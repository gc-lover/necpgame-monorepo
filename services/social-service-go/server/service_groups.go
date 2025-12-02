// Issue: #1433
package server

import (
	"context"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/pkg/api/groups"
)

// SearchGroupsResult is inline response type for searching groups
type SearchGroupsResult struct {
	Groups []groups.Group `json:"groups"`
	Total  int            `json:"total"`
}

// GetGroupMembersResult is inline response type for getting group members
type GetGroupMembersResult struct {
	Members []groups.GroupMember `json:"members"`
	Total   int                  `json:"total"`
}

// GetGroupTasksResult is inline response type for getting group tasks
type GetGroupTasksResult struct {
	Tasks []groups.GroupTask `json:"tasks"`
	Total int                `json:"total"`
}

// GroupService defines business logic for group management
type GroupService interface {
	CreateGroup(ctx context.Context, characterID string, req groups.CreateGroupRequest) (*groups.Group, error)
	SearchGroups(ctx context.Context, characterID string, params groups.SearchGroupsParams) (*SearchGroupsResult, error)
	GetGroup(ctx context.Context, groupID string) (*groups.Group, error)
	UpdateGroup(ctx context.Context, characterID string, groupID string, req groups.UpdateGroupRequest) (*groups.Group, error)
	DisbandGroup(ctx context.Context, characterID string, groupID string) error
	GetGroupMembers(ctx context.Context, groupID string) (*GetGroupMembersResult, error)
	AddGroupMember(ctx context.Context, characterID string, groupID string, req groups.AddGroupMemberRequest) (*groups.GroupMember, error)
	RemoveGroupMember(ctx context.Context, characterID string, groupID string, memberID string) error
	UpdateGroupMemberRole(ctx context.Context, characterID string, groupID string, memberID string, role groups.GroupMemberRole) (*groups.GroupMember, error)
	GetGroupTasks(ctx context.Context, groupID string) (*GetGroupTasksResult, error)
	AddGroupTask(ctx context.Context, characterID string, groupID string, taskID uuid.UUID, taskType groups.GroupTaskType) (*groups.GroupTask, error)
	UpdateGroupTask(ctx context.Context, characterID string, groupID string, taskID string, status *groups.GroupTaskStatus) (*groups.GroupTask, error)
	DeleteGroupTask(ctx context.Context, characterID string, groupID string, taskID string) error
}

// GroupServiceImpl implements GroupService
type GroupServiceImpl struct {
	repo GroupRepository
}

// NewGroupService creates new group service
func NewGroupService(repo GroupRepository) GroupService {
	return &GroupServiceImpl{repo: repo}
}

// CreateGroup creates a new group
func (s *GroupServiceImpl) CreateGroup(ctx context.Context, characterID string, req groups.CreateGroupRequest) (*groups.Group, error) {
	// Set defaults
	maxMembers := 8
	if req.MaxMembers != nil {
		maxMembers = *req.MaxMembers
	}

	if maxMembers > 8 {
		return nil, errors.New("max members cannot exceed 8")
	}

	group := &groups.Group{
		Id:          uuid.New(),
		Name:        req.Name,
		Description: req.Description,
		LeaderId:    uuid.MustParse(characterID),
		MaxMembers:  maxMembers,
		Status:      groups.GroupStatusActive,
		CreatedAt:   time.Now(),
	}

	if err := s.repo.CreateGroup(ctx, group); err != nil {
		return nil, err
	}

	// Add leader as first member
	member := &groups.GroupMember{
		CharacterId: uuid.MustParse(characterID),
		Role:        groups.Leader,
		JoinedAt:    time.Now(),
	}
	if err := s.repo.AddGroupMember(ctx, group.Id.String(), member); err != nil {
		return nil, err
	}

	return group, nil
}

// SearchGroups searches for groups
func (s *GroupServiceImpl) SearchGroups(ctx context.Context, characterID string, params groups.SearchGroupsParams) (*SearchGroupsResult, error) {
	result, err := s.repo.SearchGroups(ctx, params)
	if err != nil {
		return nil, err
	}
	return result, nil
}

// GetGroup gets group by ID
func (s *GroupServiceImpl) GetGroup(ctx context.Context, groupID string) (*groups.Group, error) {
	return s.repo.GetGroup(ctx, groupID)
}

// UpdateGroup updates group information
func (s *GroupServiceImpl) UpdateGroup(ctx context.Context, characterID string, groupID string, req groups.UpdateGroupRequest) (*groups.Group, error) {
	group, err := s.repo.GetGroup(ctx, groupID)
	if err != nil {
		return nil, err
	}

	if group.LeaderId.String() != characterID {
		return nil, errors.New("not group leader")
	}

	if req.Name != nil {
		group.Name = *req.Name
	}
	if req.Description != nil {
		group.Description = req.Description
	}

	if err := s.repo.UpdateGroup(ctx, group); err != nil {
		return nil, err
	}

	return group, nil
}

// DisbandGroup disbands a group
func (s *GroupServiceImpl) DisbandGroup(ctx context.Context, characterID string, groupID string) error {
	group, err := s.repo.GetGroup(ctx, groupID)
	if err != nil {
		return err
	}

	if group.LeaderId.String() != characterID {
		return errors.New("not group leader")
	}

	group.Status = groups.GroupStatusDisbanded
	now := time.Now()
	group.DisbandedAt = &now

	return s.repo.UpdateGroup(ctx, group)
}

// GetGroupMembers gets all group members
func (s *GroupServiceImpl) GetGroupMembers(ctx context.Context, groupID string) (*GetGroupMembersResult, error) {
	members, err := s.repo.GetGroupMembers(ctx, groupID)
	if err != nil {
		return nil, err
	}

	total := len(members)
	return &GetGroupMembersResult{
		Members: members,
		Total:   total,
	}, nil
}

// AddGroupMember adds a member to group
func (s *GroupServiceImpl) AddGroupMember(ctx context.Context, characterID string, groupID string, req groups.AddGroupMemberRequest) (*groups.GroupMember, error) {
	group, err := s.repo.GetGroup(ctx, groupID)
	if err != nil {
		return nil, err
	}

	if group.LeaderId.String() != characterID {
		return nil, errors.New("not group leader")
	}

	members, err := s.repo.GetGroupMembers(ctx, groupID)
	if err != nil {
		return nil, err
	}

	if len(members) >= group.MaxMembers {
		return nil, errors.New("group full")
	}

	member := &groups.GroupMember{
		CharacterId: req.CharacterId,
		Role:        groups.Member,
		JoinedAt:    time.Now(),
	}

	if err := s.repo.AddGroupMember(ctx, groupID, member); err != nil {
		return nil, err
	}

	return member, nil
}

// RemoveGroupMember removes a member from group
func (s *GroupServiceImpl) RemoveGroupMember(ctx context.Context, characterID string, groupID string, memberID string) error {
	group, err := s.repo.GetGroup(ctx, groupID)
	if err != nil {
		return err
	}

	if group.LeaderId.String() != characterID {
		return errors.New("not group leader")
	}

	return s.repo.RemoveGroupMember(ctx, groupID, memberID)
}

// UpdateGroupMemberRole updates member role
func (s *GroupServiceImpl) UpdateGroupMemberRole(ctx context.Context, characterID string, groupID string, memberID string, role groups.GroupMemberRole) (*groups.GroupMember, error) {
	group, err := s.repo.GetGroup(ctx, groupID)
	if err != nil {
		return nil, err
	}

	if group.LeaderId.String() != characterID {
		return nil, errors.New("not group leader")
	}

	member, err := s.repo.GetGroupMember(ctx, groupID, memberID)
	if err != nil {
		return nil, err
	}

	member.Role = role

	if err := s.repo.UpdateGroupMember(ctx, groupID, member); err != nil {
		return nil, err
	}

	return member, nil
}

// GetGroupTasks gets all group tasks
func (s *GroupServiceImpl) GetGroupTasks(ctx context.Context, groupID string) (*GetGroupTasksResult, error) {
	tasks, err := s.repo.GetGroupTasks(ctx, groupID)
	if err != nil {
		return nil, err
	}

	total := len(tasks)
	return &GetGroupTasksResult{
		Tasks: tasks,
		Total: total,
	}, nil
}

// AddGroupTask adds a task to group
func (s *GroupServiceImpl) AddGroupTask(ctx context.Context, characterID string, groupID string, taskID uuid.UUID, taskType groups.GroupTaskType) (*groups.GroupTask, error) {
	group, err := s.repo.GetGroup(ctx, groupID)
	if err != nil {
		return nil, err
	}

	if group.LeaderId.String() != characterID {
		return nil, errors.New("not group leader")
	}

	// GroupTask only has Id, Description, Status, Title fields
	title := string(taskType) // Use task type as title
	task := &groups.GroupTask{
		Id:     uuid.New(),
		Status: groups.GroupTaskStatusPending,
		Title:  &title,
	}

	if err := s.repo.AddGroupTask(ctx, groupID, task); err != nil {
		return nil, err
	}

	return task, nil
}

// UpdateGroupTask updates task status
func (s *GroupServiceImpl) UpdateGroupTask(ctx context.Context, characterID string, groupID string, taskID string, status *groups.GroupTaskStatus) (*groups.GroupTask, error) {
	group, err := s.repo.GetGroup(ctx, groupID)
	if err != nil {
		return nil, err
	}

	if group.LeaderId.String() != characterID {
		return nil, errors.New("not group leader")
	}

	task, err := s.repo.GetGroupTask(ctx, groupID, taskID)
	if err != nil {
		return nil, err
	}

	if status != nil {
		task.Status = *status
	}

	if err := s.repo.UpdateGroupTask(ctx, groupID, task); err != nil {
		return nil, err
	}

	return task, nil
}

// DeleteGroupTask deletes a task from group
func (s *GroupServiceImpl) DeleteGroupTask(ctx context.Context, characterID string, groupID string, taskID string) error {
	group, err := s.repo.GetGroup(ctx, groupID)
	if err != nil {
		return err
	}

	if group.LeaderId.String() != characterID {
		return errors.New("not group leader")
	}

	return s.repo.DeleteGroupTask(ctx, groupID, taskID)
}

