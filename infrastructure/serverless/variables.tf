# Variables for NECPGAME Serverless Infrastructure
# Enterprise-grade serverless deployment configuration

variable "aws_region" {
  description = "AWS region for serverless deployment"
  type        = string
  default     = "us-east-1"
}

variable "environment" {
  description = "Environment name (dev, staging, prod)"
  type        = string
  default     = "dev"

  validation {
    condition     = contains(["dev", "staging", "prod"], var.environment)
    error_message = "Environment must be one of: dev, staging, prod"
  }
}

variable "database_url" {
  description = "Database connection URL"
  type        = string
  sensitive   = true
}

variable "redis_url" {
  description = "Redis connection URL"
  type        = string
  sensitive   = true
}

variable "jwt_secret" {
  description = "JWT secret for authentication"
  type        = string
  sensitive   = true
}

variable "kafka_brokers" {
  description = "Kafka brokers for event streaming"
  type        = string
  sensitive   = true
}

variable "vpc_cidr" {
  description = "CIDR block for VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "availability_zones" {
  description = "List of availability zones"
  type        = list(string)
  default     = ["us-east-1a", "us-east-1b", "us-east-1c"]
}

variable "lambda_memory_size" {
  description = "Memory size for Lambda functions (MB)"
  type        = map(number)
  default = {
    "achievement-service" = 512
    "quest-service"       = 1024
    "inventory-service"   = 512
    "economy-service"     = 1024
    "combat-stats-service" = 512
    "analytics-service"   = 2048
  }
}

variable "lambda_timeout" {
  description = "Timeout for Lambda functions (seconds)"
  type        = map(number)
  default = {
    "achievement-service" = 30
    "quest-service"       = 60
    "inventory-service"   = 30
    "economy-service"     = 60
    "combat-stats-service" = 30
    "analytics-service"   = 300
  }
}

variable "tags" {
  description = "Common tags for all resources"
  type        = map(string)
  default = {
    Project     = "NECPGAME"
    Environment = "serverless"
    ManagedBy   = "Terraform"
    Owner       = "NECPGAME Team"
  }
}

variable "cognito_user_pool_config" {
  description = "Cognito User Pool configuration"
  type = object({
    password_min_length    = optional(number, 8)
    require_uppercase      = optional(bool, true)
    require_lowercase      = optional(bool, true)
    require_numbers        = optional(bool, true)
    require_symbols        = optional(bool, true)
    auto_verified_attributes = optional(list(string), ["email"])
  })
  default = {
    password_min_length     = 8
    require_uppercase       = true
    require_lowercase       = true
    require_numbers         = true
    require_symbols         = true
    auto_verified_attributes = ["email"]
  }
}

variable "api_gateway_config" {
  description = "API Gateway configuration"
  type = object({
    stage_name = optional(string, "prod")
    throttling_settings = optional(object({
      burst_limit = optional(number, 100)
      rate_limit  = optional(number, 50)
    }), {
      burst_limit = 100
      rate_limit  = 50
    })
  })
  default = {
    stage_name = "prod"
    throttling_settings = {
      burst_limit = 100
      rate_limit  = 50
    }
  }
}

variable "dynamodb_config" {
  description = "DynamoDB configuration"
  type = object({
    billing_mode = optional(string, "PAY_PER_REQUEST")
    read_capacity = optional(number, 5)
    write_capacity = optional(number, 5)
    enable_point_in_time_recovery = optional(bool, true)
    enable_encryption = optional(bool, true)
  })
  default = {
    billing_mode = "PAY_PER_REQUEST"
    read_capacity = 5
    write_capacity = 5
    enable_point_in_time_recovery = true
    enable_encryption = true
  }
}

variable "s3_config" {
  description = "S3 bucket configuration"
  type = object({
    versioning_enabled = optional(bool, true)
    encryption_enabled = optional(bool, true)
    lifecycle_rules = optional(list(object({
      enabled = bool
      prefix  = string
      expiration = optional(object({
        days = number
      }), null)
      noncurrent_version_expiration = optional(object({
        days = number
      }), null)
    })), [])
  })
  default = {
    versioning_enabled = true
    encryption_enabled = true
    lifecycle_rules = []
  }
}
