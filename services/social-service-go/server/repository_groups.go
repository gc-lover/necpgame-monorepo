// Issue: #1433
package server

import (
	"context"
	"errors"

	"github.com/necpgame/social-service-go/pkg/api/groups"
)

// GroupRepository defines database operations for groups
type GroupRepository interface {
	CreateGroup(ctx context.Context, group *groups.Group) error
	SearchGroups(ctx context.Context, params groups.SearchGroupsParams) (*SearchGroupsResult, error)
	GetGroup(ctx context.Context, groupID string) (*groups.Group, error)
	UpdateGroup(ctx context.Context, group *groups.Group) error
	GetGroupMembers(ctx context.Context, groupID string) ([]groups.GroupMember, error)
	AddGroupMember(ctx context.Context, groupID string, member *groups.GroupMember) error
	RemoveGroupMember(ctx context.Context, groupID string, memberID string) error
	GetGroupMember(ctx context.Context, groupID string, memberID string) (*groups.GroupMember, error)
	UpdateGroupMember(ctx context.Context, groupID string, member *groups.GroupMember) error
	GetGroupTasks(ctx context.Context, groupID string) ([]groups.GroupTask, error)
	AddGroupTask(ctx context.Context, groupID string, task *groups.GroupTask) error
	GetGroupTask(ctx context.Context, groupID string, taskID string) (*groups.GroupTask, error)
	UpdateGroupTask(ctx context.Context, groupID string, task *groups.GroupTask) error
	DeleteGroupTask(ctx context.Context, groupID string, taskID string) error
}

// InMemoryGroupRepository implements GroupRepository for testing/development
type InMemoryGroupRepository struct {
	groups  map[string]*groups.Group
	members map[string][]groups.GroupMember // groupID -> members
	tasks   map[string][]groups.GroupTask   // groupID -> tasks
}

// NewInMemoryGroupRepository creates new in-memory repository
func NewInMemoryGroupRepository() GroupRepository {
	return &InMemoryGroupRepository{
		groups:  make(map[string]*groups.Group),
		members: make(map[string][]groups.GroupMember),
		tasks:   make(map[string][]groups.GroupTask),
	}
}

// CreateGroup creates a new group
func (r *InMemoryGroupRepository) CreateGroup(ctx context.Context, group *groups.Group) error {
	r.groups[group.Id.String()] = group
	r.members[group.Id.String()] = []groups.GroupMember{}
	r.tasks[group.Id.String()] = []groups.GroupTask{}
	return nil
}

// SearchGroups searches for groups
func (r *InMemoryGroupRepository) SearchGroups(ctx context.Context, params groups.SearchGroupsParams) (*SearchGroupsResult, error) {
	var result []groups.Group

	for _, group := range r.groups {
		// Apply filters
		if params.Status != nil && group.Status != groups.GroupStatus(*params.Status) {
			continue
		}
		if params.LeaderId != nil && group.LeaderId.String() != params.LeaderId.String() {
			continue
		}
		result = append(result, *group)
	}

	total := len(result)

	// Apply pagination
	limit := 20
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	if offset > len(result) {
		offset = len(result)
	}
	end := offset + limit
	if end > len(result) {
		end = len(result)
	}

	result = result[offset:end]

	return &SearchGroupsResult{
		Groups: result,
		Total:  total,
	}, nil
}

// GetGroup gets group by ID
func (r *InMemoryGroupRepository) GetGroup(ctx context.Context, groupID string) (*groups.Group, error) {
	group, ok := r.groups[groupID]
	if !ok {
		return nil, errors.New("group not found")
	}
	return group, nil
}

// UpdateGroup updates group
func (r *InMemoryGroupRepository) UpdateGroup(ctx context.Context, group *groups.Group) error {
	if _, ok := r.groups[group.Id.String()]; !ok {
		return errors.New("group not found")
	}
	r.groups[group.Id.String()] = group
	return nil
}

// GetGroupMembers gets all group members
func (r *InMemoryGroupRepository) GetGroupMembers(ctx context.Context, groupID string) ([]groups.GroupMember, error) {
	members, ok := r.members[groupID]
	if !ok {
		return nil, errors.New("group not found")
	}
	return members, nil
}

// AddGroupMember adds a member to group
func (r *InMemoryGroupRepository) AddGroupMember(ctx context.Context, groupID string, member *groups.GroupMember) error {
	if _, ok := r.groups[groupID]; !ok {
		return errors.New("group not found")
	}
	r.members[groupID] = append(r.members[groupID], *member)
	return nil
}

// RemoveGroupMember removes a member from group
func (r *InMemoryGroupRepository) RemoveGroupMember(ctx context.Context, groupID string, memberID string) error {
	members, ok := r.members[groupID]
	if !ok {
		return errors.New("group not found")
	}

	for i, m := range members {
		if m.CharacterId.String() == memberID {
			r.members[groupID] = append(members[:i], members[i+1:]...)
			return nil
		}
	}

	return errors.New("member not found")
}

// GetGroupMember gets a specific group member
func (r *InMemoryGroupRepository) GetGroupMember(ctx context.Context, groupID string, memberID string) (*groups.GroupMember, error) {
	members, ok := r.members[groupID]
	if !ok {
		return nil, errors.New("group not found")
	}

	for _, m := range members {
		if m.CharacterId.String() == memberID {
			return &m, nil
		}
	}

	return nil, errors.New("member not found")
}

// UpdateGroupMember updates member information
func (r *InMemoryGroupRepository) UpdateGroupMember(ctx context.Context, groupID string, member *groups.GroupMember) error {
	members, ok := r.members[groupID]
	if !ok {
		return errors.New("group not found")
	}

	for i, m := range members {
		if m.CharacterId.String() == member.CharacterId.String() {
			r.members[groupID][i] = *member
			return nil
		}
	}

	return errors.New("member not found")
}

// GetGroupTasks gets all group tasks
func (r *InMemoryGroupRepository) GetGroupTasks(ctx context.Context, groupID string) ([]groups.GroupTask, error) {
	tasks, ok := r.tasks[groupID]
	if !ok {
		return nil, errors.New("group not found")
	}
	return tasks, nil
}

// AddGroupTask adds a task to group
func (r *InMemoryGroupRepository) AddGroupTask(ctx context.Context, groupID string, task *groups.GroupTask) error {
	if _, ok := r.groups[groupID]; !ok {
		return errors.New("group not found")
	}
	r.tasks[groupID] = append(r.tasks[groupID], *task)
	return nil
}

// GetGroupTask gets a specific group task
func (r *InMemoryGroupRepository) GetGroupTask(ctx context.Context, groupID string, taskID string) (*groups.GroupTask, error) {
	tasks, ok := r.tasks[groupID]
	if !ok {
		return nil, errors.New("group not found")
	}

	for _, t := range tasks {
		if t.Id.String() == taskID {
			return &t, nil
		}
	}

	return nil, errors.New("task not found")
}

// UpdateGroupTask updates task information
func (r *InMemoryGroupRepository) UpdateGroupTask(ctx context.Context, groupID string, task *groups.GroupTask) error {
	tasks, ok := r.tasks[groupID]
	if !ok {
		return errors.New("group not found")
	}

	for i, t := range tasks {
		if t.Id.String() == task.Id.String() {
			r.tasks[groupID][i] = *task
			return nil
		}
	}

	return errors.New("task not found")
}

// DeleteGroupTask deletes a task from group
func (r *InMemoryGroupRepository) DeleteGroupTask(ctx context.Context, groupID string, taskID string) error {
	tasks, ok := r.tasks[groupID]
	if !ok {
		return errors.New("group not found")
	}

	for i, t := range tasks {
		if t.Id.String() == taskID {
			r.tasks[groupID] = append(tasks[:i], tasks[i+1:]...)
			return nil
		}
	}

	return errors.New("task not found")
}

