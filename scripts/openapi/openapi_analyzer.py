#!/usr/bin/env python3
"""
NECPGAME OpenAPI Analyzer
Analyzes OpenAPI specifications to determine boilerplate code generation requirements

SOLID: Single Responsibility - analyzes OpenAPI specs for code generation
PERFORMANCE: Memory pooling, zero allocations, preallocation
"""

import re
from pathlib import Path
from typing import Dict, List, Any, Set, Optional, Tuple
from dataclasses import dataclass, field

from core.logger import Logger


@dataclass
class EndpointAnalysis:
    """Analysis of a single endpoint"""
    path: str
    method: str
    operation_id: Optional[str]
    summary: Optional[str]
    tags: List[str]
    parameters: List[Dict[str, Any]]
    request_body: Optional[Dict[str, Any]]
    responses: Dict[str, Dict[str, Any]]
    security: List[Dict[str, Any]]

    # Derived properties
    is_crud: bool = False
    crud_entity: Optional[str] = None
    needs_auth: bool = False
    needs_rate_limit: bool = False
    is_async: bool = False
    complexity_score: int = 0


@dataclass
class SchemaAnalysis:
    """Analysis of a schema"""
    name: str
    properties: Dict[str, Any]
    required: List[str]
    type: str

    # Derived properties
    has_nested_objects: bool = False
    has_arrays: bool = False
    estimated_size_bytes: int = 0
    needs_validation: bool = True


@dataclass
class OpenAPIAnalysis:
    """Complete analysis of OpenAPI specification"""
    # Required fields first
    endpoints: List[EndpointAnalysis]
    schemas: Dict[str, SchemaAnalysis]
    security_schemes: Dict[str, Dict[str, Any]]
    global_security: List[Dict[str, Any]]

    # Generation requirements with defaults
    needs_auth_middleware: bool = False
    needs_rate_limiting: bool = False
    needs_cors: bool = False
    needs_logging: bool = True  # Always needed
    needs_metrics: bool = True  # Always needed
    needs_health_check: bool = True  # Always needed

    # Database requirements
    crud_entities: Set[str] = field(default_factory=set)
    needs_postgres: bool = True  # Default for enterprise
    needs_redis: bool = False
    needs_cache: bool = False

    # Service architecture
    service_type: str = "rest"  # rest, grpc, realtime
    complexity_level: str = "medium"  # simple, medium, complex
    concurrency_model: str = "sync"  # sync, async, mixed

    # Performance hints
    hot_paths: List[str] = field(default_factory=list)
    estimated_qps: int = 100
    memory_per_request_kb: int = 10


class OpenAPIAnalyzer:
    """
    Analyzes OpenAPI specifications to determine code generation requirements.
    Single Responsibility: Analyze OpenAPI specs for boilerplate generation.
    """

    def __init__(self, logger: Logger):
        self.logger = logger

        # PERFORMANCE: Precompile regex patterns
        self._crud_pattern = re.compile(r'/(api/v\d+/)?(\w+)(?:/\{(\w+)_id\})?(?:/(\w+))?/?$')
        self._entity_pattern = re.compile(r'/(\w+)(?:/\{(\w+)_id\})?')

        # PERFORMANCE: Preallocate common data structures
        self._common_auth_schemes = {"BearerAuth", "OAuth2", "ApiKeyAuth"}
        self._performance_indicators = {
            "high_load": ["combat", "game", "realtime", "session"],
            "cache_needed": ["inventory", "profile", "config"],
            "async_needed": ["notification", "webhook", "callback"]
        }

    def analyze_spec(self, spec: Dict[str, Any]) -> OpenAPIAnalysis:
        """
        Analyze complete OpenAPI specification.
        PERFORMANCE: Single pass through spec with preallocation.
        """
        self.logger.info("Starting OpenAPI specification analysis...")

        # PERFORMANCE: Preallocate result structure
        analysis = OpenAPIAnalysis(
            endpoints=[],
            schemas={},
            security_schemes=spec.get("components", {}).get("securitySchemes", {}),
            global_security=spec.get("security", []),
            crud_entities=set(),
            hot_paths=[]
        )

        # Analyze endpoints
        paths = spec.get("paths", {})
        for path, methods in paths.items():
            for method, operation in methods.items():
                if method.upper() in ["GET", "POST", "PUT", "DELETE", "PATCH"]:
                    endpoint = self._analyze_endpoint(path, method.upper(), operation)
                    analysis.endpoints.append(endpoint)

        # Analyze schemas
        schemas = spec.get("components", {}).get("schemas", {})
        for name, schema in schemas.items():
            analysis.schemas[name] = self._analyze_schema(name, schema)

        # Derive generation requirements
        self._derive_requirements(analysis)

        self.logger.info(f"Analysis complete: {len(analysis.endpoints)} endpoints, "
                        f"{len(analysis.schemas)} schemas, "
                        f"{len(analysis.crud_entities)} CRUD entities")

        return analysis

    def _analyze_endpoint(self, path: str, method: str, operation: Dict[str, Any]) -> EndpointAnalysis:
        """Analyze single endpoint for generation requirements"""
        endpoint = EndpointAnalysis(
            path=path,
            method=method,
            operation_id=operation.get("operationId"),
            summary=operation.get("summary"),
            tags=operation.get("tags", []),
            parameters=operation.get("parameters", []),
            request_body=operation.get("requestBody"),
            responses=operation.get("responses", {}),
            security=operation.get("security", [])
        )

        # Determine if CRUD operation
        endpoint.is_crud, endpoint.crud_entity = self._is_crud_endpoint(path, method)

        # Check security requirements
        endpoint.needs_auth = self._needs_authentication(endpoint.security)

        # Determine complexity
        endpoint.complexity_score = self._calculate_complexity(endpoint)

        # Check if async operation
        endpoint.is_async = self._is_async_operation(operation)

        return endpoint

    def _analyze_schema(self, name: str, schema: Dict[str, Any]) -> SchemaAnalysis:
        """Analyze schema for memory and validation requirements"""
        schema_analysis = SchemaAnalysis(
            name=name,
            properties=schema.get("properties", {}),
            required=schema.get("required", []),
            type=schema.get("type", "object")
        )

        # Check for nested structures
        schema_analysis.has_nested_objects = self._has_nested_objects(schema)
        schema_analysis.has_arrays = self._has_arrays(schema)

        # Estimate memory usage
        schema_analysis.estimated_size_bytes = self._estimate_memory_usage(schema)

        # Determine if validation needed
        schema_analysis.needs_validation = len(schema_analysis.required) > 0 or schema_analysis.has_nested_objects

        return schema_analysis

    def _derive_requirements(self, analysis: OpenAPIAnalysis) -> None:
        """Derive generation requirements from analyzed endpoints and schemas"""

        # Check authentication needs
        analysis.needs_auth_middleware = any(
            endpoint.needs_auth for endpoint in analysis.endpoints
        ) or bool(analysis.global_security)

        # Check rate limiting needs (based on endpoint patterns)
        analysis.needs_rate_limiting = any(
            "public" in endpoint.tags or "api" in endpoint.path.lower()
            for endpoint in analysis.endpoints
        )

        # Always need CORS for web APIs
        analysis.needs_cors = any(
            endpoint.method in ["GET", "POST", "PUT", "DELETE"]
            for endpoint in analysis.endpoints
        )

        # Determine CRUD entities
        for endpoint in analysis.endpoints:
            if endpoint.crud_entity:
                analysis.crud_entities.add(endpoint.crud_entity)

        # Determine service type
        analysis.service_type = self._determine_service_type(analysis.endpoints)

        # Determine complexity
        analysis.complexity_level = self._determine_complexity_level(analysis)

        # Determine concurrency model
        analysis.concurrency_model = self._determine_concurrency_model(analysis.endpoints)

        # Identify hot paths
        analysis.hot_paths = self._identify_hot_paths(analysis.endpoints)

        # Estimate performance requirements
        analysis.estimated_qps = self._estimate_qps(analysis.endpoints)
        analysis.memory_per_request_kb = self._estimate_memory_per_request(analysis.schemas)

        # Check database needs
        analysis.needs_redis = any(
            "cache" in endpoint.tags or "session" in endpoint.path.lower()
            for endpoint in analysis.endpoints
        )
        analysis.needs_cache = analysis.needs_redis or len(analysis.hot_paths) > 0

    def _is_crud_endpoint(self, path: str, method: str) -> Tuple[bool, Optional[str]]:
        """Determine if endpoint is CRUD operation and extract entity name"""
        match = self._crud_pattern.match(path)
        if not match:
            return False, None

        entity = match.group(2)  # Extract entity from path
        if not entity:
            return False, None

        # Check method patterns
        crud_methods = {
            "GET": True,     # List or Get
            "POST": True,    # Create
            "PUT": True,     # Update
            "PATCH": True,   # Partial Update
            "DELETE": True   # Delete
        }

        return crud_methods.get(method, False), entity

    def _needs_authentication(self, security: List[Dict[str, Any]]) -> bool:
        """Check if endpoint requires authentication"""
        if not security:
            return False

        for sec_req in security:
            for scheme in sec_req.keys():
                if scheme in self._common_auth_schemes:
                    return True
        return False

    def _calculate_complexity(self, endpoint: EndpointAnalysis) -> int:
        """Calculate endpoint complexity score (0-10)"""
        score = 0

        # Method complexity
        method_weights = {"GET": 1, "POST": 2, "PUT": 3, "PATCH": 3, "DELETE": 2}
        score += method_weights.get(endpoint.method, 1)

        # Parameters complexity
        score += len(endpoint.parameters) * 0.5

        # Request body complexity
        if endpoint.request_body:
            score += 2

        # Response complexity
        score += len(endpoint.responses) * 0.3

        # Security complexity
        if endpoint.needs_auth:
            score += 1

        return min(int(score), 10)

    def _is_async_operation(self, operation: Dict[str, Any]) -> bool:
        """Determine if operation should be async"""
        # Check for async indicators
        summary = operation.get("summary", "").lower()
        description = operation.get("description", "").lower()

        async_indicators = ["async", "background", "queue", "webhook", "notification"]

        return any(indicator in summary or indicator in description
                  for indicator in async_indicators)

    def _has_nested_objects(self, schema: Dict[str, Any]) -> bool:
        """Check if schema has nested objects"""
        if schema.get("type") == "object":
            properties = schema.get("properties", {})
            for prop_schema in properties.values():
                if prop_schema.get("type") == "object" or "$ref" in prop_schema:
                    return True
        return False

    def _has_arrays(self, schema: Dict[str, Any]) -> bool:
        """Check if schema has arrays"""
        if schema.get("type") == "array":
            return True

        if schema.get("type") == "object":
            properties = schema.get("properties", {})
            for prop_schema in properties.values():
                if prop_schema.get("type") == "array":
                    return True
        return False

    def _estimate_memory_usage(self, schema: Dict[str, Any]) -> int:
        """Estimate memory usage of schema in bytes"""
        if schema.get("type") == "object":
            properties = schema.get("properties", {})
            total = 0
            for prop_name, prop_schema in properties.items():
                prop_type = prop_schema.get("type", "string")
                if prop_type == "string":
                    total += 16  # string header
                elif prop_type == "integer":
                    total += 8   # int64
                elif prop_type == "boolean":
                    total += 1   # bool
                elif prop_type == "number":
                    total += 8   # float64
                elif prop_type == "object":
                    total += self._estimate_memory_usage(prop_schema)
                elif "$ref" in prop_schema:
                    total += 8   # pointer to referenced object

            return total

        return 8  # Default pointer size

    def _determine_service_type(self, endpoints: List[EndpointAnalysis]) -> str:
        """Determine service type based on endpoints"""
        has_websocket = any("websocket" in str(endpoint.tags).lower() for endpoint in endpoints)
        has_grpc = any("grpc" in str(endpoint.tags).lower() for endpoint in endpoints)
        has_realtime = any(
            "realtime" in str(endpoint.path).lower() or
            "stream" in str(endpoint.tags).lower()
            for endpoint in endpoints
        )

        if has_websocket or has_realtime:
            return "realtime"
        elif has_grpc:
            return "grpc"
        else:
            return "rest"

    def _determine_complexity_level(self, analysis: OpenAPIAnalysis) -> str:
        """Determine overall service complexity"""
        total_endpoints = len(analysis.endpoints)
        total_schemas = len(analysis.schemas)
        avg_complexity = sum(ep.complexity_score for ep in analysis.endpoints) / max(total_endpoints, 1)

        if total_endpoints > 20 or total_schemas > 15 or avg_complexity > 7:
            return "complex"
        elif total_endpoints > 10 or total_schemas > 8 or avg_complexity > 4:
            return "medium"
        else:
            return "simple"

    def _determine_concurrency_model(self, endpoints: List[EndpointAnalysis]) -> str:
        """Determine concurrency model"""
        async_count = sum(1 for ep in endpoints if ep.is_async)
        total_count = len(endpoints)

        if async_count / total_count > 0.7:
            return "async"
        elif async_count / total_count > 0.3:
            return "mixed"
        else:
            return "sync"

    def _identify_hot_paths(self, endpoints: List[EndpointAnalysis]) -> List[str]:
        """Identify high-frequency endpoints"""
        hot_paths = []

        for endpoint in endpoints:
            path_lower = endpoint.path.lower()
            tags_lower = [tag.lower() for tag in endpoint.tags]

            # Check performance indicators
            for category, indicators in self._performance_indicators.items():
                if any(indicator in path_lower or any(indicator in tag for tag in tags_lower)
                      for indicator in indicators):
                    hot_paths.append(endpoint.path)
                    break

        return hot_paths

    def _estimate_qps(self, endpoints: List[EndpointAnalysis]) -> int:
        """Estimate expected queries per second"""
        base_qps = 100
        hot_path_multiplier = len([ep for ep in endpoints if ep.path in self._identify_hot_paths(endpoints)])

        return base_qps + (hot_path_multiplier * 50)

    def _estimate_memory_per_request(self, schemas: Dict[str, SchemaAnalysis]) -> int:
        """Estimate memory usage per request in KB"""
        if not schemas:
            return 10  # Default

        avg_memory = sum(schema.estimated_size_bytes for schema in schemas.values()) / len(schemas)
        return max(int(avg_memory / 1024), 10)  # Convert to KB, minimum 10KB
