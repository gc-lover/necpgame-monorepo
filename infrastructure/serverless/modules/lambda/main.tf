# Lambda Module for NECPGAME Serverless Services
# Enterprise-grade Lambda function configuration with performance optimization

variable "function_name" {
  description = "Name of the Lambda function"
  type        = string
}

variable "vpc_config" {
  description = "VPC configuration for Lambda function"
  type = object({
    subnet_ids         = list(string)
    security_group_ids = list(string)
  })
  default = null
}

variable "environment_variables" {
  description = "Environment variables for Lambda function"
  type        = map(string)
  default     = {}
}

variable "memory_size" {
  description = "Memory size for Lambda function (MB)"
  type        = number
  default     = 1024
}

variable "timeout" {
  description = "Timeout for Lambda function (seconds)"
  type        = number
  default     = 30
}

variable "tags" {
  description = "Tags for Lambda function"
  type        = map(string)
  default     = {}
}

# IAM Role for Lambda
resource "aws_iam_role" "lambda_role" {
  name = "necpgame-${var.function_name}-lambda-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "lambda.amazonaws.com"
        }
      }
    ]
  })

  tags = var.tags
}

# IAM Policy for Lambda
resource "aws_iam_role_policy" "lambda_policy" {
  name = "necpgame-${var.function_name}-lambda-policy"
  role = aws_iam_role.lambda_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents"
        ]
        Resource = "arn:aws:logs:*:*:*"
      },
      {
        Effect = "Allow"
        Action = [
          "ssm:GetParameter",
          "ssm:GetParameters",
          "ssm:GetParametersByPath"
        ]
        Resource = "arn:aws:ssm:*:*:parameter/necpgame/serverless/*"
      },
      {
        Effect = "Allow"
        Action = [
          "dynamodb:GetItem",
          "dynamodb:PutItem",
          "dynamodb:UpdateItem",
          "dynamodb:DeleteItem",
          "dynamodb:Query",
          "dynamodb:Scan"
        ]
        Resource = "arn:aws:dynamodb:*:*:table/necpgame-*"
      },
      {
        Effect = "Allow"
        Action = [
          "s3:GetObject",
          "s3:PutObject",
          "s3:DeleteObject"
        ]
        Resource = "arn:aws:s3:::necpgame-serverless-*/*"
      },
      {
        Effect = "Allow"
        Action = [
          "events:PutEvents"
        ]
        Resource = "arn:aws:events:*:*:event-bus/necpgame-*"
      },
      {
        Effect = "Allow"
        Action = [
          "kafka:DescribeCluster",
          "kafka:GetBootstrapBrokers"
        ]
        Resource = "*"
      }
    ]
  })
}

# Lambda Function
resource "aws_lambda_function" "this" {
  function_name = "necpgame-${var.function_name}"
  runtime       = "provided.al2"
  handler       = "bootstrap"
  memory_size   = var.memory_size
  timeout       = var.timeout

  # Use a placeholder package for now - will be replaced with actual deployment
  filename         = data.archive_file.placeholder.output_path
  source_code_hash = data.archive_file.placeholder.output_base64sha256

  role = aws_iam_role.lambda_role.arn

  dynamic "vpc_config" {
    for_each = var.vpc_config != null ? [var.vpc_config] : []
    content {
      subnet_ids         = vpc_config.value.subnet_ids
      security_group_ids = vpc_config.value.security_group_ids
    }
  }

  environment {
    variables = merge(
      var.environment_variables,
      {
        SERVICE_NAME = var.function_name
        ENVIRONMENT  = "serverless"
      }
    )
  }

  tags = var.tags

  depends_on = [
    aws_iam_role_policy.lambda_policy
  ]
}

# CloudWatch Log Group
resource "aws_cloudwatch_log_group" "lambda_logs" {
  name              = "/aws/lambda/necpgame-${var.function_name}"
  retention_in_days = 30

  tags = var.tags
}

# Placeholder deployment package
data "archive_file" "placeholder" {
  type        = "zip"
  output_path = "${path.module}/placeholder.zip"

  source {
    content  = "#!/bin/bash\necho 'NECPGAME ${var.function_name} Lambda function placeholder'\n"
    filename = "bootstrap"
  }
}

# Outputs
output "function_name" {
  description = "Lambda function name"
  value       = aws_lambda_function.this.function_name
}

output "function_arn" {
  description = "Lambda function ARN"
  value       = aws_lambda_function.this.arn
}

output "invoke_arn" {
  description = "Lambda function invoke ARN"
  value       = aws_lambda_function.this.invoke_arn
}

output "role_arn" {
  description = "IAM role ARN for Lambda function"
  value       = aws_iam_role.lambda_role.arn
}
