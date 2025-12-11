package api

// Minimal optional/value stubs for generated params.

type GetPlayerOrdersStatus string
type GetPlayerOrdersOrderType string

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

// OptInt is optional int.
type OptInt struct {
	Value int
	Set   bool
}

func NewOptInt(v int) OptInt { return OptInt{Value: v, Set: true} }
func (o OptInt) IsSet() bool { return o.Set }
func (o *OptInt) Reset()     { o.Value = 0; o.Set = false }

// BearerAuth matches ogen auth schema.
type BearerAuth struct {
	Token string
	Roles []string
}


