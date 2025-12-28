// Package backup provides comprehensive backup and disaster recovery for MMOFPS systems
package backup

import (
	"context"
	"crypto/sha256"
	"encoding/json"
	"fmt"
	"io"
	"os"
	"path/filepath"
	"sort"
	"strings"
	"sync"
	"time"

	"go.uber.org/zap"

	errorhandling "github.com/your-org/necpgame/scripts/core/error-handling"
)

// BackupManager handles backup and recovery operations
type BackupManager struct {
	config     *BackupConfig
	logger     *errorhandling.Logger
	backups    map[string]*BackupMetadata
	restores   map[string]*RestoreOperation

	mu sync.RWMutex

	// Storage backends
	storages map[string]StorageBackend

	// Performance metrics
	totalBackups    int64
	totalRestores   int64
	totalSizeBytes  int64
	lastBackupTime  time.Time
}

// BackupConfig holds backup configuration
type BackupConfig struct {
	BackupDir         string
	RetentionDays     int
	MaxConcurrentJobs int
	CompressionLevel  int
	EncryptionKey     string
	Storages          map[string]StorageConfig
	Schedules         []BackupSchedule
}

// StorageConfig defines storage backend configuration
type StorageConfig struct {
	Type   string                 `json:"type"`
	Config map[string]interface{} `json:"config"`
}

// BackupSchedule defines when backups should run
type BackupSchedule struct {
	Name         string
	DataType     string
	CronSchedule string
	Retention    int // days
	Enabled      bool
}

// BackupMetadata contains metadata about a backup
type BackupMetadata struct {
	ID           string            `json:"id"`
	DataType     string            `json:"data_type"`
	CreatedAt    time.Time         `json:"created_at"`
	SizeBytes    int64             `json:"size_bytes"`
	Checksum     string            `json:"checksum"`
	Version      string            `json:"version"`
	Status       BackupStatus      `json:"status"`
	Location     string            `json:"location"`
	Compression  bool              `json:"compression"`
	Encryption   bool              `json:"encryption"`
	Metadata     map[string]interface{} `json:"metadata,omitempty"`
}

// BackupStatus represents the status of a backup
type BackupStatus string

const (
	BackupStatusPending    BackupStatus = "pending"
	BackupStatusRunning    BackupStatus = "running"
	BackupStatusCompleted  BackupStatus = "completed"
	BackupStatusFailed     BackupStatus = "failed"
	BackupStatusCorrupted  BackupStatus = "corrupted"
)

// RestoreOperation represents a restore operation
type RestoreOperation struct {
	ID            string            `json:"id"`
	BackupID      string            `json:"backup_id"`
	DataType      string            `json:"data_type"`
	Status        RestoreStatus     `json:"status"`
	StartedAt     time.Time         `json:"started_at"`
	CompletedAt   *time.Time        `json:"completed_at,omitempty"`
	Progress      float64           `json:"progress"`
	ErrorMessage  string            `json:"error_message,omitempty"`
	Metadata      map[string]interface{} `json:"metadata,omitempty"`
}

// RestoreStatus represents the status of a restore operation
type RestoreStatus string

const (
	RestoreStatusPending   RestoreStatus = "pending"
	RestoreStatusRunning   RestoreStatus = "running"
	RestoreStatusCompleted RestoreStatus = "completed"
	RestoreStatusFailed    RestoreStatus = "failed"
)

// StorageBackend defines interface for storage backends
type StorageBackend interface {
	Store(ctx context.Context, key string, reader io.Reader, metadata map[string]interface{}) error
	Retrieve(ctx context.Context, key string) (io.ReadCloser, map[string]interface{}, error)
	Delete(ctx context.Context, key string) error
	List(ctx context.Context, prefix string) ([]string, error)
}

// DataSource defines interface for data sources that can be backed up
type DataSource interface {
	Name() string
	Backup(ctx context.Context, writer io.Writer) error
	Restore(ctx context.Context, reader io.Reader) error
	GetMetadata() map[string]interface{}
}

// DatabaseDataSource implements DataSource for PostgreSQL databases
type DatabaseDataSource struct {
	ConnectionString string
	DatabaseName     string
	Tables           []string
	logger           *errorhandling.Logger
}

// NewBackupManager creates a new backup manager
func NewBackupManager(config *BackupConfig, logger *errorhandling.Logger) (*BackupManager, error) {
	bm := &BackupManager{
		config:   config,
		logger:   logger,
		backups:  make(map[string]*BackupMetadata),
		restores: make(map[string]*RestoreOperation),
		storages: make(map[string]StorageBackend),
	}

	// Initialize storage backends
	for name, storageConfig := range config.Storages {
		backend, err := bm.createStorageBackend(storageConfig)
		if err != nil {
			return nil, fmt.Errorf("failed to create storage backend %s: %w", name, err)
		}
		bm.storages[name] = backend
	}

	// Load existing backup metadata
	if err := bm.loadBackupMetadata(); err != nil {
		logger.Warnw("Failed to load backup metadata", "error", err)
	}

	// Start background cleanup
	go bm.cleanupRoutine()

	return bm, nil
}

// CreateBackup creates a new backup
func (bm *BackupManager) CreateBackup(ctx context.Context, dataSource DataSource, storageName string, options BackupOptions) (*BackupMetadata, error) {
	bm.mu.Lock()
	backupID := fmt.Sprintf("backup_%d", time.Now().UnixNano())
	metadata := &BackupMetadata{
		ID:          backupID,
		DataType:    dataSource.Name(),
		CreatedAt:   time.Now(),
		Status:      BackupStatusPending,
		Compression: options.Compression,
		Encryption:  options.Encryption,
		Metadata:    dataSource.GetMetadata(),
	}
	bm.backups[backupID] = metadata
	bm.mu.Unlock()

	// Run backup in background
	go bm.runBackup(ctx, dataSource, metadata, storageName, options)

	bm.logger.Infow("Backup creation started",
		"backup_id", backupID,
		"data_type", dataSource.Name(),
		"storage", storageName)

	return metadata, nil
}

// RestoreBackup restores data from a backup
func (bm *BackupManager) RestoreBackup(ctx context.Context, backupID string, dataSource DataSource, options RestoreOptions) (*RestoreOperation, error) {
	bm.mu.RLock()
	backup, exists := bm.backups[backupID]
	bm.mu.RUnlock()

	if !exists {
		return nil, errorhandling.NewNotFoundError("BACKUP_NOT_FOUND", "Backup not found")
	}

	if backup.Status != BackupStatusCompleted {
		return nil, errorhandling.NewValidationError("BACKUP_NOT_READY", "Backup is not ready for restore")
	}

	bm.mu.Lock()
	restoreID := fmt.Sprintf("restore_%d", time.Now().UnixNano())
	restore := &RestoreOperation{
		ID:        restoreID,
		BackupID:  backupID,
		DataType:  backup.DataType,
		Status:    RestoreStatusPending,
		StartedAt: time.Now(),
		Progress:  0.0,
	}
	bm.restores[restoreID] = restore
	bm.mu.Unlock()

	// Run restore in background
	go bm.runRestore(ctx, backup, dataSource, restore, options)

	bm.logger.Infow("Restore operation started",
		"restore_id", restoreID,
		"backup_id", backupID,
		"data_type", backup.DataType)

	return restore, nil
}

// GetBackupStatus gets the status of a backup
func (bm *BackupManager) GetBackupStatus(backupID string) (*BackupMetadata, error) {
	bm.mu.RLock()
	defer bm.mu.RUnlock()

	backup, exists := bm.backups[backupID]
	if !exists {
		return nil, errorhandling.NewNotFoundError("BACKUP_NOT_FOUND", "Backup not found")
	}

	return backup, nil
}

// GetRestoreStatus gets the status of a restore operation
func (bm *BackupManager) GetRestoreStatus(restoreID string) (*RestoreOperation, error) {
	bm.mu.RLock()
	defer bm.mu.RUnlock()

	restore, exists := bm.restores[restoreID]
	if !exists {
		return nil, errorhandling.NewNotFoundError("RESTORE_NOT_FOUND", "Restore operation not found")
	}

	return restore, nil
}

// ListBackups lists all backups with optional filtering
func (bm *BackupManager) ListBackups(dataType string, limit int) []*BackupMetadata {
	bm.mu.RLock()
	defer bm.mu.RUnlock()

	var backups []*BackupMetadata
	for _, backup := range bm.backups {
		if dataType == "" || backup.DataType == dataType {
			backups = append(backups, backup)
		}
	}

	// Sort by creation time (newest first)
	sort.Slice(backups, func(i, j int) bool {
		return backups[i].CreatedAt.After(backups[j].CreatedAt)
	})

	if limit > 0 && len(backups) > limit {
		backups = backups[:limit]
	}

	return backups
}

// DeleteBackup deletes a backup
func (bm *BackupManager) DeleteBackup(ctx context.Context, backupID string) error {
	bm.mu.Lock()
	defer bm.mu.Unlock()

	backup, exists := bm.backups[backupID]
	if !exists {
		return errorhandling.NewNotFoundError("BACKUP_NOT_FOUND", "Backup not found")
	}

	// Delete from storage
	if storage, exists := bm.storages[backup.Location]; exists {
		if err := storage.Delete(ctx, backupID); err != nil {
			bm.logger.Warnw("Failed to delete backup from storage", "backup_id", backupID, "error", err)
		}
	}

	// Delete local metadata
	delete(bm.backups, backupID)

	// Save updated metadata
	if err := bm.saveBackupMetadata(); err != nil {
		bm.logger.Warnw("Failed to save backup metadata after deletion", "error", err)
	}

	bm.logger.Infow("Backup deleted", "backup_id", backupID)
	return nil
}

// GetBackupStats returns backup statistics
func (bm *BackupManager) GetBackupStats() map[string]interface{} {
	bm.mu.RLock()
	defer bm.mu.RUnlock()

	totalSize := int64(0)
	completedBackups := 0
	failedBackups := 0
	dataTypes := make(map[string]int)

	for _, backup := range bm.backups {
		totalSize += backup.SizeBytes
		if backup.Status == BackupStatusCompleted {
			completedBackups++
		} else if backup.Status == BackupStatusFailed {
			failedBackups++
		}
		dataTypes[backup.DataType]++
	}

	activeRestores := 0
	for _, restore := range bm.restores {
		if restore.Status == RestoreStatusRunning || restore.Status == RestoreStatusPending {
			activeRestores++
		}
	}

	return map[string]interface{}{
		"total_backups":      len(bm.backups),
		"completed_backups":  completedBackups,
		"failed_backups":     failedBackups,
		"total_size_bytes":   totalSize,
		"data_types":         dataTypes,
		"active_restores":    activeRestores,
		"last_backup_time":   bm.lastBackupTime,
	}
}

// runBackup executes the backup process
func (bm *BackupManager) runBackup(ctx context.Context, dataSource DataSource, metadata *BackupMetadata, storageName string, options BackupOptions) {
	metadata.Status = BackupStatusRunning

	// Create temporary file for backup
	tempFile, err := os.CreateTemp("", fmt.Sprintf("backup_%s_*", metadata.DataType))
	if err != nil {
		bm.updateBackupStatus(metadata.ID, BackupStatusFailed, 0, "")
		bm.logger.Errorw("Failed to create temp file for backup", "backup_id", metadata.ID, "error", err)
		return
	}
	defer os.Remove(tempFile.Name())
	defer tempFile.Close()

	// Perform backup
	startTime := time.Now()
	checksum := sha256.New()

	multiWriter := io.MultiWriter(tempFile, checksum)

	if err := dataSource.Backup(ctx, multiWriter); err != nil {
		bm.updateBackupStatus(metadata.ID, BackupStatusFailed, 0, err.Error())
		bm.logger.Errorw("Backup failed", "backup_id", metadata.ID, "error", err)
		return
	}

	// Get file size
	fileInfo, err := tempFile.Stat()
	if err != nil {
		bm.updateBackupStatus(metadata.ID, BackupStatusFailed, 0, err.Error())
		return
	}

	fileSize := fileInfo.Size()

	// Apply compression if requested
	if options.Compression {
		compressedFile, err := bm.compressFile(tempFile.Name())
		if err != nil {
			bm.updateBackupStatus(metadata.ID, BackupStatusFailed, 0, err.Error())
			return
		}
		tempFile.Close()
		tempFile, err = os.Open(compressedFile)
		if err != nil {
			bm.updateBackupStatus(metadata.ID, BackupStatusFailed, 0, err.Error())
			return
		}
		defer os.Remove(compressedFile)
	}

	// Apply encryption if requested
	if options.Encryption {
		encryptedFile, err := bm.encryptFile(tempFile.Name(), bm.config.EncryptionKey)
		if err != nil {
			bm.updateBackupStatus(metadata.ID, BackupStatusFailed, 0, err.Error())
			return
		}
		tempFile.Close()
		tempFile, err = os.Open(encryptedFile)
		if err != nil {
			bm.updateBackupStatus(metadata.ID, BackupStatusFailed, 0, err.Error())
			return
		}
		defer os.Remove(encryptedFile)
	}

	// Store backup
	storage, exists := bm.storages[storageName]
	if !exists {
		bm.updateBackupStatus(metadata.ID, BackupStatusFailed, 0, "Storage backend not found")
		return
	}

	if _, err := tempFile.Seek(0, 0); err != nil {
		bm.updateBackupStatus(metadata.ID, BackupStatusFailed, 0, err.Error())
		return
	}

	storageMetadata := map[string]interface{}{
		"data_type":    metadata.DataType,
		"created_at":   metadata.CreatedAt,
		"compression":  options.Compression,
		"encryption":   options.Encryption,
	}

	if err := storage.Store(ctx, metadata.ID, tempFile, storageMetadata); err != nil {
		bm.updateBackupStatus(metadata.ID, BackupStatusFailed, 0, err.Error())
		return
	}

	// Update metadata
	duration := time.Since(startTime)
	metadata.SizeBytes = fileSize
	metadata.Checksum = fmt.Sprintf("%x", checksum.Sum(nil))
	metadata.Status = BackupStatusCompleted
	metadata.Location = storageName

	bm.mu.Lock()
	bm.totalBackups++
	bm.totalSizeBytes += fileSize
	bm.lastBackupTime = time.Now()
	bm.mu.Unlock()

	if err := bm.saveBackupMetadata(); err != nil {
		bm.logger.Warnw("Failed to save backup metadata", "error", err)
	}

	bm.logger.Infow("Backup completed successfully",
		"backup_id", metadata.ID,
		"data_type", metadata.DataType,
		"size_bytes", fileSize,
		"duration", duration,
		"storage", storageName)
}

// runRestore executes the restore process
func (bm *BackupManager) runRestore(ctx context.Context, backup *BackupMetadata, dataSource DataSource, restore *RestoreOperation, options RestoreOptions) {
	restore.Status = RestoreStatusRunning

	// Get backup from storage
	storage, exists := bm.storages[backup.Location]
	if !exists {
		bm.updateRestoreStatus(restore.ID, RestoreStatusFailed, "Storage backend not found")
		return
	}

	reader, _, err := storage.Retrieve(ctx, backup.ID)
	if err != nil {
		bm.updateRestoreStatus(restore.ID, RestoreStatusFailed, err.Error())
		return
	}
	defer reader.Close()

	// Apply decryption if needed
	if backup.Encryption {
		decryptedReader, err := bm.decryptReader(reader, bm.config.EncryptionKey)
		if err != nil {
			bm.updateRestoreStatus(restore.ID, RestoreStatusFailed, err.Error())
			return
		}
		reader = decryptedReader
	}

	// Apply decompression if needed
	if backup.Compression {
		decompressedReader, err := bm.decompressReader(reader)
		if err != nil {
			bm.updateRestoreStatus(restore.ID, RestoreStatusFailed, err.Error())
			return
		}
		reader = decompressedReader
	}

	// Perform restore
	if err := dataSource.Restore(ctx, reader); err != nil {
		bm.updateRestoreStatus(restore.ID, RestoreStatusFailed, err.Error())
		return
	}

	// Mark as completed
	now := time.Now()
	restore.Status = RestoreStatusCompleted
	restore.CompletedAt = &now
	restore.Progress = 100.0

	bm.mu.Lock()
	bm.totalRestores++
	bm.mu.Unlock()

	bm.logger.Infow("Restore completed successfully",
		"restore_id", restore.ID,
		"backup_id", backup.ID,
		"data_type", backup.DataType)
}

// updateBackupStatus updates backup status
func (bm *BackupManager) updateBackupStatus(backupID string, status BackupStatus, sizeBytes int64, errorMessage string) {
	bm.mu.Lock()
	defer bm.mu.Unlock()

	if backup, exists := bm.backups[backupID]; exists {
		backup.Status = status
		if sizeBytes > 0 {
			backup.SizeBytes = sizeBytes
		}
		if errorMessage != "" {
			backup.Metadata["error"] = errorMessage
		}
	}

	if err := bm.saveBackupMetadata(); err != nil {
		bm.logger.Warnw("Failed to save backup metadata", "error", err)
	}
}

// updateRestoreStatus updates restore status
func (bm *BackupManager) updateRestoreStatus(restoreID string, status RestoreStatus, errorMessage string) {
	bm.mu.Lock()
	defer bm.mu.Unlock()

	if restore, exists := bm.restores[restoreID]; exists {
		restore.Status = status
		if errorMessage != "" {
			restore.ErrorMessage = errorMessage
		}
	}
}

// Helper methods (implementation would include actual compression/encryption)
func (bm *BackupManager) compressFile(filename string) (string, error) {
	// Implementation for file compression
	return filename + ".gz", nil
}

func (bm *BackupManager) encryptFile(filename, key string) (string, error) {
	// Implementation for file encryption
	return filename + ".enc", nil
}

func (bm *BackupManager) decryptReader(reader io.Reader, key string) (io.Reader, error) {
	// Implementation for decryption
	return reader, nil
}

func (bm *BackupManager) decompressReader(reader io.Reader) (io.Reader, error) {
	// Implementation for decompression
	return reader, nil
}

func (bm *BackupManager) createStorageBackend(config StorageConfig) (StorageBackend, error) {
	// Implementation for different storage backends (S3, GCS, local, etc.)
	switch config.Type {
	case "local":
		return NewLocalStorageBackend(config.Config), nil
	case "s3":
		return NewS3StorageBackend(config.Config), nil
	default:
		return nil, fmt.Errorf("unsupported storage type: %s", config.Type)
	}
}

func (bm *BackupManager) loadBackupMetadata() error {
	metadataFile := filepath.Join(bm.config.BackupDir, "backup_metadata.json")
	if _, err := os.Stat(metadataFile); os.IsNotExist(err) {
		return nil // No existing metadata
	}

	data, err := os.ReadFile(metadataFile)
	if err != nil {
		return err
	}

	return json.Unmarshal(data, &bm.backups)
}

func (bm *BackupManager) saveBackupMetadata() error {
	metadataFile := filepath.Join(bm.config.BackupDir, "backup_metadata.json")

	data, err := json.MarshalIndent(bm.backups, "", "  ")
	if err != nil {
		return err
	}

	return os.WriteFile(metadataFile, data, 0644)
}

func (bm *BackupManager) cleanupRoutine() {
	ticker := time.NewTicker(24 * time.Hour) // Daily cleanup
	defer ticker.Stop()

	for range ticker.C {
		if err := bm.cleanupOldBackups(); err != nil {
			bm.logger.Warnw("Failed to cleanup old backups", "error", err)
		}
	}
}

func (bm *BackupManager) cleanupOldBackups() error {
	bm.mu.Lock()
	defer bm.mu.Unlock()

	cutoff := time.Now().AddDate(0, 0, -bm.config.RetentionDays)
	var toDelete []string

	for id, backup := range bm.backups {
		if backup.CreatedAt.Before(cutoff) {
			toDelete = append(toDelete, id)
		}
	}

	for _, id := range toDelete {
		delete(bm.backups, id)
		bm.logger.Infow("Cleaned up old backup", "backup_id", id)
	}

	return bm.saveBackupMetadata()
}

// BackupOptions defines options for backup operations
type BackupOptions struct {
	Compression bool
	Encryption  bool
	Verify      bool
	Parallel    bool
}

// RestoreOptions defines options for restore operations
type RestoreOptions struct {
	Verify      bool
	DryRun      bool
	Parallel    bool
}

// Shutdown gracefully shuts down the backup manager
func (bm *BackupManager) Shutdown(ctx context.Context) error {
	bm.logger.Info("Backup manager shutting down")
	return nil
}
