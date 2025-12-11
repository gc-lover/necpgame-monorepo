package api

// Minimal optional/value stubs for generated params.

type GetPlayerOrdersStatus string
type GetPlayerOrdersOrderType string
type CreatePlayerOrderRequestOrderType string
type PlayerOrderOrderType string
type PlayerOrderStatus string

// OptGetPlayerOrdersStatus is optional GetPlayerOrdersStatus.
type OptGetPlayerOrdersStatus struct {
	Value GetPlayerOrdersStatus
	Set   bool
}

func NewOptGetPlayerOrdersStatus(v GetPlayerOrdersStatus) OptGetPlayerOrdersStatus {
	return OptGetPlayerOrdersStatus{Value: v, Set: true}
}

func (o OptGetPlayerOrdersStatus) IsSet() bool { return o.Set }
func (o *OptGetPlayerOrdersStatus) Reset()     { o.Value = ""; o.Set = false }
func (o OptGetPlayerOrdersStatus) Get() (GetPlayerOrdersStatus, bool) {
	return o.Value, o.Set
}
func (o *OptGetPlayerOrdersStatus) SetTo(v GetPlayerOrdersStatus) {
	o.Value, o.Set = v, true
}

// OptGetPlayerOrdersOrderType is optional GetPlayerOrdersOrderType.
type OptGetPlayerOrdersOrderType struct {
	Value GetPlayerOrdersOrderType
	Set   bool
}

func NewOptGetPlayerOrdersOrderType(v GetPlayerOrdersOrderType) OptGetPlayerOrdersOrderType {
	return OptGetPlayerOrdersOrderType{Value: v, Set: true}
}

func (o OptGetPlayerOrdersOrderType) IsSet() bool { return o.Set }
func (o *OptGetPlayerOrdersOrderType) Reset()     { o.Value = ""; o.Set = false }
func (o OptGetPlayerOrdersOrderType) Get() (GetPlayerOrdersOrderType, bool) {
	return o.Value, o.Set
}
func (o *OptGetPlayerOrdersOrderType) SetTo(v GetPlayerOrdersOrderType) {
	o.Value, o.Set = v, true
}

// Create request and order models (minimal to satisfy validators).
type CreatePlayerOrderRequest struct {
	OrderType CreatePlayerOrderRequestOrderType `json:"order_type"`
}

type PlayerOrder struct {
	OrderType OptPlayerOrderOrderType `json:"order_type"`
	Status    OptPlayerOrderStatus    `json:"status"`
}

type PlayerOrdersResponse struct {
	Orders []PlayerOrder `json:"orders"`
}

// Optional wrappers for player order fields.
type OptPlayerOrderOrderType struct {
	Value PlayerOrderOrderType
	Set   bool
}

func NewOptPlayerOrderOrderType(v PlayerOrderOrderType) OptPlayerOrderOrderType {
	return OptPlayerOrderOrderType{Value: v, Set: true}
}
func (o OptPlayerOrderOrderType) IsSet() bool { return o.Set }
func (o *OptPlayerOrderOrderType) Reset()     { o.Value = ""; o.Set = false }
func (o OptPlayerOrderOrderType) Get() (PlayerOrderOrderType, bool) {
	return o.Value, o.Set
}
func (o *OptPlayerOrderOrderType) SetTo(v PlayerOrderOrderType) {
	o.Value, o.Set = v, true
}

type OptPlayerOrderStatus struct {
	Value PlayerOrderStatus
	Set   bool
}

func NewOptPlayerOrderStatus(v PlayerOrderStatus) OptPlayerOrderStatus {
	return OptPlayerOrderStatus{Value: v, Set: true}
}
func (o OptPlayerOrderStatus) IsSet() bool { return o.Set }
func (o *OptPlayerOrderStatus) Reset()     { o.Value = ""; o.Set = false }
func (o OptPlayerOrderStatus) Get() (PlayerOrderStatus, bool) {
	return o.Value, o.Set
}
func (o *OptPlayerOrderStatus) SetTo(v PlayerOrderStatus) {
	o.Value, o.Set = v, true
}

// OptInt is optional int.
type OptInt struct {
	Value int
	Set   bool
}

func NewOptInt(v int) OptInt      { return OptInt{Value: v, Set: true} }
func (o OptInt) IsSet() bool      { return o.Set }
func (o *OptInt) Reset()          { o.Value = 0; o.Set = false }
func (o OptInt) Get() (int, bool) { return o.Value, o.Set }
func (o *OptInt) SetTo(v int)     { o.Value, o.Set = v, true }

// BearerAuth matches ogen auth schema.
type BearerAuth struct {
	Token string
	Roles []string
}
