// Package server Issue: #1433, #1604
package server

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/necpgame/social-service-go/pkg/api/groups"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

// Constants moved to handlers.go to avoid duplication

// GroupHandlers implements groups.ServerInterface
type GroupHandlers struct {
	logger  *logrus.Logger
	service GroupService
}

// NewGroupHandlers creates new group handlers

// CreateGroup implements POST /social/groups
func (h *GroupHandlers) CreateGroup(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	var req groups.CreateGroupRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Get authenticated character ID from context
	characterID := getCharacterIDFromContext(ctx)
	if characterID == "" {
		respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	group, err := h.service.CreateGroup(ctx, characterID, req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create group")
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusCreated, group)
}

// SearchGroups implements GET /social/groups
func (h *GroupHandlers) SearchGroups(w http.ResponseWriter, r *http.Request, params groups.SearchGroupsParams) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	characterID := getCharacterIDFromContext(ctx)
	if characterID == "" {
		respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	result, err := h.service.SearchGroups(ctx, characterID, params)
	if err != nil {
		h.logger.WithError(err).Error("Failed to search groups")
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, result)
}

// GetGroup implements GET /social/groups/{group_id}
func (h *GroupHandlers) GetGroup(w http.ResponseWriter, r *http.Request, groupId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	characterID := getCharacterIDFromContext(ctx)
	if characterID == "" {
		respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	group, err := h.service.GetGroup(ctx, groupId.String())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get group")
		respondError(w, http.StatusNotFound, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, group)
}

// UpdateGroup implements PUT /social/groups/{group_id}
func (h *GroupHandlers) UpdateGroup(w http.ResponseWriter, r *http.Request, groupId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	var req groups.UpdateGroupRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	characterID := getCharacterIDFromContext(ctx)
	if characterID == "" {
		respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	group, err := h.service.UpdateGroup(ctx, characterID, groupId.String(), req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update group")
		if err.Error() == "not group leader" {
			respondError(w, http.StatusForbidden, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, group)
}

// DisbandGroup implements DELETE /social/groups/{group_id}
func (h *GroupHandlers) DisbandGroup(w http.ResponseWriter, r *http.Request, groupId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	characterID := getCharacterIDFromContext(ctx)
	if characterID == "" {
		respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	err := h.service.DisbandGroup(ctx, characterID, groupId.String())
	if err != nil {
		h.logger.WithError(err).Error("Failed to disband group")
		if err.Error() == "not group leader" {
			respondError(w, http.StatusForbidden, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{"message": "Group disbanded"})
}

// GetGroupMembers implements GET /social/groups/{group_id}/members
func (h *GroupHandlers) GetGroupMembers(w http.ResponseWriter, r *http.Request, groupId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	characterID := getCharacterIDFromContext(ctx)
	if characterID == "" {
		respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	result, err := h.service.GetGroupMembers(ctx, groupId.String())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get group members")
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, result)
}

// AddGroupMember implements POST /social/groups/{group_id}/members
func (h *GroupHandlers) AddGroupMember(w http.ResponseWriter, r *http.Request, groupId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	var req groups.AddGroupMemberRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	characterID := getCharacterIDFromContext(ctx)
	if characterID == "" {
		respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	member, err := h.service.AddGroupMember(ctx, characterID, groupId.String(), req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to add group member")
		if err.Error() == "not group leader" {
			respondError(w, http.StatusForbidden, err.Error())
			return
		}
		if err.Error() == "group full" {
			respondError(w, http.StatusBadRequest, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusCreated, member)
}

// RemoveGroupMember implements DELETE /social/groups/{group_id}/members/{member_id}
func (h *GroupHandlers) RemoveGroupMember(w http.ResponseWriter, r *http.Request, groupId openapi_types.UUID, memberId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	characterID := getCharacterIDFromContext(ctx)
	if characterID == "" {
		respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	err := h.service.RemoveGroupMember(ctx, characterID, groupId.String(), memberId.String())
	if err != nil {
		h.logger.WithError(err).Error("Failed to remove group member")
		if err.Error() == "not group leader" {
			respondError(w, http.StatusForbidden, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{"message": "Member removed"})
}

// UpdateGroupMemberRole implements PUT /social/groups/{group_id}/members/{member_id}/role
func (h *GroupHandlers) UpdateGroupMemberRole(w http.ResponseWriter, r *http.Request, groupId openapi_types.UUID, memberId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	var req struct {
		Role groups.GroupMemberRole `json:"role"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	characterID := getCharacterIDFromContext(ctx)
	if characterID == "" {
		respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	member, err := h.service.UpdateGroupMemberRole(ctx, characterID, groupId.String(), memberId.String(), req.Role)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update member role")
		if err.Error() == "not group leader" {
			respondError(w, http.StatusForbidden, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, member)
}

// GetGroupTasks implements GET /social/groups/{group_id}/tasks
func (h *GroupHandlers) GetGroupTasks(w http.ResponseWriter, r *http.Request, groupId openapi_types.UUID, _ groups.GetGroupTasksParams) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	characterID := getCharacterIDFromContext(ctx)
	if characterID == "" {
		respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	result, err := h.service.GetGroupTasks(ctx, groupId.String())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get group tasks")
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, result)
}

// AddGroupTask implements POST /social/groups/{group_id}/tasks
func (h *GroupHandlers) AddGroupTask(w http.ResponseWriter, r *http.Request, groupId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	var req struct {
		TaskId   openapi_types.UUID   `json:"task_id"`
		TaskType groups.GroupTaskType `json:"task_type"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	characterID := getCharacterIDFromContext(ctx)
	if characterID == "" {
		respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	task, err := h.service.AddGroupTask(ctx, characterID, groupId.String(), req.TaskId, req.TaskType)
	if err != nil {
		h.logger.WithError(err).Error("Failed to add group task")
		if err.Error() == "not group leader" {
			respondError(w, http.StatusForbidden, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusCreated, task)
}

// UpdateGroupTask implements PUT /social/groups/{group_id}/tasks/{task_id}
func (h *GroupHandlers) UpdateGroupTask(w http.ResponseWriter, r *http.Request, groupId openapi_types.UUID, taskId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	var req struct {
		Status *groups.GroupTaskStatus `json:"status,omitempty"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	characterID := getCharacterIDFromContext(ctx)
	if characterID == "" {
		respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	task, err := h.service.UpdateGroupTask(ctx, characterID, groupId.String(), taskId.String(), req.Status)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update group task")
		if err.Error() == "not group leader" {
			respondError(w, http.StatusForbidden, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, task)
}

// DeleteGroupTask implements DELETE /social/groups/{group_id}/tasks/{task_id}
func (h *GroupHandlers) DeleteGroupTask(w http.ResponseWriter, r *http.Request, groupId openapi_types.UUID, taskId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	characterID := getCharacterIDFromContext(ctx)
	if characterID == "" {
		respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	err := h.service.DeleteGroupTask(ctx, characterID, groupId.String(), taskId.String())
	if err != nil {
		h.logger.WithError(err).Error("Failed to delete group task")
		if err.Error() == "not group leader" {
			respondError(w, http.StatusForbidden, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{"message": "Task deleted"})
}
