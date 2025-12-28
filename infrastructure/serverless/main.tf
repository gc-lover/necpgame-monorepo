# Serverless Infrastructure Setup for NECPGAME
# Enterprise-grade serverless deployment with AWS Lambda and API Gateway
# Issue: #2218 - Serverless Infrastructure Setup

terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  backend "s3" {
    bucket = "necpgame-terraform-state"
    key    = "serverless/terraform.tfstate"
    region = "us-east-1"
  }
}

provider "aws" {
  region = var.aws_region
}

# VPC and Networking for Serverless
module "vpc" {
  source = "terraform-aws-modules/vpc/aws"

  name = "necpgame-serverless-vpc"
  cidr = "10.0.0.0/16"

  azs             = ["${var.aws_region}a", "${var.aws_region}b", "${var.aws_region}c"]
  private_subnets = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
  public_subnets  = ["10.0.101.0/24", "10.0.102.0/24", "10.0.103.0/24"]

  enable_nat_gateway = true
  single_nat_gateway = true

  tags = {
    Environment = "serverless"
    Project     = "NECPGAME"
  }
}

# Lambda Functions for Core Services
module "lambda_functions" {
  source = "./modules/lambda"

  for_each = toset([
    "achievement-service",
    "quest-service",
    "inventory-service",
    "economy-service",
    "combat-stats-service",
    "analytics-service"
  ])

  function_name = each.key
  vpc_config = {
    subnet_ids         = module.vpc.private_subnets
    security_group_ids = [aws_security_group.lambda_sg.id]
  }

  environment_variables = {
    DATABASE_URL     = aws_ssm_parameter.database_url.arn
    REDIS_URL        = aws_ssm_parameter.redis_url.arn
    JWT_SECRET       = aws_ssm_parameter.jwt_secret.arn
    KAFKA_BROKERS    = aws_ssm_parameter.kafka_brokers.arn
  }

  tags = {
    Environment = "serverless"
    Service     = each.key
  }
}

# API Gateway for REST APIs
resource "aws_api_gateway_rest_api" "necpgame_api" {
  name        = "necpgame-serverless-api"
  description = "Serverless API Gateway for NECPGAME microservices"

  endpoint_configuration {
    types = ["REGIONAL"]
  }

  tags = {
    Environment = "serverless"
    Project     = "NECPGAME"
  }
}

# API Gateway Resources and Methods
resource "aws_api_gateway_resource" "achievement" {
  rest_api_id = aws_api_gateway_rest_api.necpgame_api.id
  parent_id   = aws_api_gateway_rest_api.necpgame_api.root_resource_id
  path_part   = "achievement"
}

resource "aws_api_gateway_method" "achievement_post" {
  rest_api_id   = aws_api_gateway_rest_api.necpgame_api.id
  resource_id   = aws_api_gateway_resource.achievement.id
  http_method   = "POST"
  authorization = "COGNITO_USER_POOLS"
  authorizer_id = aws_api_gateway_authorizer.cognito.id
}

resource "aws_api_gateway_integration" "achievement_lambda" {
  rest_api_id             = aws_api_gateway_rest_api.necpgame_api.id
  resource_id             = aws_api_gateway_resource.achievement.id
  http_method             = aws_api_gateway_method.achievement_post.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = module.lambda_functions["achievement-service"].invoke_arn
}

# Lambda Permissions for API Gateway
resource "aws_lambda_permission" "api_gateway_achievement" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = module.lambda_functions["achievement-service"].function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_api_gateway_rest_api.necpgame_api.execution_arn}/*/*/*"
}

# Cognito User Pool for Authentication
resource "aws_cognito_user_pool" "necpgame_users" {
  name = "necpgame-serverless-users"

  password_policy {
    minimum_length    = 8
    require_uppercase = true
    require_lowercase = true
    require_numbers   = true
    require_symbols   = true
  }

  auto_verified_attributes = ["email"]
}

resource "aws_cognito_user_pool_client" "client" {
  name         = "necpgame-client"
  user_pool_id = aws_cognito_user_pool.necpgame_users.id

  explicit_auth_flows = [
    "ALLOW_USER_PASSWORD_AUTH",
    "ALLOW_REFRESH_TOKEN_AUTH",
    "ALLOW_USER_SRP_AUTH"
  ]
}

# API Gateway Authorizer
resource "aws_api_gateway_authorizer" "cognito" {
  name          = "cognito-authorizer"
  type          = "COGNITO_USER_POOLS"
  rest_api_id   = aws_api_gateway_rest_api.necpgame_api.id
  provider_arns = [aws_cognito_user_pool.necpgame_users.arn]
}

# EventBridge for Event-Driven Architecture
resource "aws_cloudwatch_event_bus" "necpgame_events" {
  name = "necpgame-serverless-events"
}

resource "aws_cloudwatch_event_rule" "achievement_events" {
  name        = "achievement-events"
  event_bus_name = aws_cloudwatch_event_bus.necpgame_events.name

  event_pattern = jsonencode({
    source = ["necpgame.achievement"]
    detail-type = ["Achievement Unlocked"]
  })
}

resource "aws_cloudwatch_event_target" "achievement_lambda_target" {
  rule      = aws_cloudwatch_event_rule.achievement_events.name
  event_bus_name = aws_cloudwatch_event_bus.necpgame_events.name
  arn       = module.lambda_functions["analytics-service"].arn
}

# Lambda permissions for EventBridge
resource "aws_lambda_permission" "eventbridge_analytics" {
  statement_id  = "AllowEventBridgeInvoke"
  action        = "lambda:InvokeFunction"
  function_name = module.lambda_functions["analytics-service"].function_name
  principal     = "events.amazonaws.com"
  source_arn    = aws_cloudwatch_event_rule.achievement_events.arn
}

# DynamoDB for Serverless Data Storage
resource "aws_dynamodb_table" "game_sessions" {
  name         = "necpgame-sessions"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "session_id"

  attribute {
    name = "session_id"
    type = "S"
  }

  attribute {
    name = "player_id"
    type = "S"
  }

  global_secondary_index {
    name            = "PlayerSessionsIndex"
    hash_key        = "player_id"
    projection_type = "ALL"
  }

  tags = {
    Environment = "serverless"
    Project     = "NECPGAME"
  }
}

# S3 Buckets for Static Assets and Logs
resource "aws_s3_bucket" "game_assets" {
  bucket = "necpgame-serverless-assets-${var.environment}"

  tags = {
    Environment = var.environment
    Project     = "NECPGAME"
  }
}

resource "aws_s3_bucket_versioning" "game_assets_versioning" {
  bucket = aws_s3_bucket.game_assets.id
  versioning_configuration {
    status = "Enabled"
  }
}

# CloudFront Distribution for Global CDN
resource "aws_cloudfront_distribution" "game_assets" {
  origin {
    domain_name = aws_s3_bucket.game_assets.bucket_regional_domain_name
    origin_id   = "S3-necpgame-assets"
  }

  enabled             = true
  is_ipv6_enabled     = true
  default_root_object = "index.html"

  default_cache_behavior {
    allowed_methods  = ["GET", "HEAD", "OPTIONS"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = "S3-necpgame-assets"

    forwarded_values {
      query_string = false
      cookies {
        forward = "none"
      }
    }

    viewer_protocol_policy = "redirect-to-https"
    min_ttl                = 0
    default_ttl            = 86400
    max_ttl                = 31536000
  }

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }

  tags = {
    Environment = var.environment
    Project     = "NECPGAME"
  }
}

# Security Group for Lambda Functions
resource "aws_security_group" "lambda_sg" {
  name_prefix = "necpgame-lambda-"
  vpc_id      = module.vpc.vpc_id

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Environment = "serverless"
    Project     = "NECPGAME"
  }
}

# SSM Parameters for Secrets
resource "aws_ssm_parameter" "database_url" {
  name        = "/necpgame/serverless/database_url"
  description = "Database connection URL for serverless services"
  type        = "SecureString"
  value       = var.database_url

  tags = {
    Environment = "serverless"
    Project     = "NECPGAME"
  }
}

resource "aws_ssm_parameter" "redis_url" {
  name        = "/necpgame/serverless/redis_url"
  description = "Redis connection URL for serverless services"
  type        = "SecureString"
  value       = var.redis_url

  tags = {
    Environment = "serverless"
    Project     = "NECPGAME"
  }
}

resource "aws_ssm_parameter" "jwt_secret" {
  name        = "/necpgame/serverless/jwt_secret"
  description = "JWT secret for serverless services"
  type        = "SecureString"
  value       = var.jwt_secret

  tags = {
    Environment = "serverless"
    Project     = "NECPGAME"
  }
}

resource "aws_ssm_parameter" "kafka_brokers" {
  name        = "/necpgame/serverless/kafka_brokers"
  description = "Kafka brokers for serverless event streaming"
  type        = "SecureString"
  value       = var.kafka_brokers

  tags = {
    Environment = "serverless"
    Project     = "NECPGAME"
  }
}

# CloudWatch Alarms for Monitoring
resource "aws_cloudwatch_metric_alarm" "lambda_errors" {
  for_each = toset([
    "achievement-service",
    "quest-service",
    "inventory-service",
    "economy-service",
    "combat-stats-service",
    "analytics-service"
  ])

  alarm_name          = "necpgame-${each.key}-errors"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "2"
  metric_name         = "Errors"
  namespace           = "AWS/Lambda"
  period              = "300"
  statistic           = "Sum"
  threshold           = "5"
  alarm_description   = "This metric monitors lambda errors for ${each.key}"

  dimensions = {
    FunctionName = module.lambda_functions[each.key].function_name
  }

  tags = {
    Environment = "serverless"
    Service     = each.key
  }
}

# API Gateway Deployment and Stage
resource "aws_api_gateway_deployment" "necpgame_prod" {
  depends_on = [
    aws_api_gateway_integration.achievement_lambda,
  ]

  rest_api_id = aws_api_gateway_rest_api.necpgame_api.id
  stage_name  = "prod"

  variables = {
    deployed_at = timestamp()
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_api_gateway_stage" "prod" {
  deployment_id = aws_api_gateway_deployment.necpgame_prod.id
  rest_api_id   = aws_api_gateway_rest_api.necpgame_api.id
  stage_name    = "prod"
}

# CodePipeline for CI/CD
resource "aws_codepipeline" "lambda_pipeline" {
  name     = "necpgame-lambda-pipeline"
  role_arn = aws_iam_role.codepipeline_role.arn

  artifact_store {
    location = aws_s3_bucket.codepipeline_bucket.bucket
    type     = "S3"
  }

  stage {
    name = "Source"

    action {
      name             = "Source"
      category         = "Source"
      owner            = "AWS"
      provider         = "CodeCommit"
      version          = "1"
      output_artifacts = ["source_output"]

      configuration = {
        RepositoryName = "necpgame-monorepo"
        BranchName     = "develop"
      }
    }
  }

  stage {
    name = "Build"

    action {
      name             = "Build"
      category         = "Build"
      owner            = "AWS"
      provider         = "CodeBuild"
      input_artifacts  = ["source_output"]
      output_artifacts = ["build_output"]
      version          = "1"

      configuration = {
        ProjectName = aws_codebuild_project.lambda_build.name
      }
    }
  }

  stage {
    name = "Deploy"

    action {
      name            = "Deploy"
      category        = "Deploy"
      owner           = "AWS"
      provider        = "CloudFormation"
      input_artifacts = ["build_output"]
      version         = "1"

      configuration = {
        ActionMode    = "REPLACE_ON_FAILURE"
        Capabilities  = "CAPABILITY_IAM,CAPABILITY_AUTO_EXPAND"
        StackName     = "necpgame-serverless-stack"
        TemplatePath  = "build_output::template.yaml"
      }
    }
  }
}

# CodeBuild for Lambda builds
resource "aws_codebuild_project" "lambda_build" {
  name          = "necpgame-lambda-build"
  description   = "Build Lambda functions for NECPGAME serverless"
  service_role  = aws_iam_role.codebuild_role.arn

  artifacts {
    type = "CODEPIPELINE"
  }

  environment {
    compute_type                = "BUILD_GENERAL1_SMALL"
    image                       = "aws/codebuild/amazonlinux2-x86_64-standard:3.0"
    type                        = "LINUX_CONTAINER"
    image_pull_credentials_type = "CODEBUILD"

    environment_variable {
      name  = "GO_VERSION"
      value = "1.21"
    }
  }

  source {
    type = "CODEPIPELINE"
  }

  tags = {
    Environment = "serverless"
    Project     = "NECPGAME"
  }
}

# S3 bucket for CodePipeline artifacts
resource "aws_s3_bucket" "codepipeline_bucket" {
  bucket = "necpgame-codepipeline-artifacts-${var.environment}"

  tags = {
    Environment = var.environment
    Project     = "NECPGAME"
  }
}

resource "aws_s3_bucket_versioning" "codepipeline_versioning" {
  bucket = aws_s3_bucket.codepipeline_bucket.id
  versioning_configuration {
    status = "Enabled"
  }
}

# IAM Roles for CI/CD
resource "aws_iam_role" "codepipeline_role" {
  name = "necpgame-codepipeline-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "codepipeline.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy" "codepipeline_policy" {
  name = "necpgame-codepipeline-policy"
  role = aws_iam_role.codepipeline_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "s3:GetObject",
          "s3:GetObjectVersion",
          "s3:GetBucketVersioning",
          "s3:PutObject"
        ]
        Resource = [
          aws_s3_bucket.codepipeline_bucket.arn,
          "${aws_s3_bucket.codepipeline_bucket.arn}/*"
        ]
      },
      {
        Effect = "Allow"
        Action = [
          "codebuild:BatchGetBuilds",
          "codebuild:StartBuild"
        ]
        Resource = aws_codebuild_project.lambda_build.arn
      },
      {
        Effect = "Allow"
        Action = [
          "cloudformation:CreateStack",
          "cloudformation:UpdateStack",
          "cloudformation:DeleteStack",
          "cloudformation:DescribeStacks",
          "cloudformation:CreateChangeSet",
          "cloudformation:ExecuteChangeSet"
        ]
        Resource = "arn:aws:cloudformation:*:*:stack/necpgame-*"
      },
      {
        Effect = "Allow"
        Action = [
          "iam:PassRole"
        ]
        Resource = aws_iam_role.lambda_role.arn
      }
    ]
  })
}

resource "aws_iam_role" "codebuild_role" {
  name = "necpgame-codebuild-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "codebuild.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy" "codebuild_policy" {
  name = "necpgame-codebuild-policy"
  role = aws_iam_role.codebuild_role.id

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
          "s3:GetObject",
          "s3:PutObject",
          "s3:GetObjectVersion"
        ]
        Resource = [
          aws_s3_bucket.codepipeline_bucket.arn,
          "${aws_s3_bucket.codepipeline_bucket.arn}/*"
        ]
      },
      {
        Effect = "Allow"
        Action = [
          "ssm:GetParameter",
          "ssm:GetParameters"
        ]
        Resource = "arn:aws:ssm:*:*:parameter/necpgame/*"
      }
    ]
  })
}

# Outputs
output "api_gateway_url" {
  description = "API Gateway URL for serverless services"
  value       = aws_api_gateway_deployment.necpgame_prod.invoke_url
}

output "cloudfront_distribution_url" {
  description = "CloudFront distribution URL for game assets"
  value       = aws_cloudfront_distribution.game_assets.domain_name
}

output "cognito_user_pool_id" {
  description = "Cognito User Pool ID for authentication"
  value       = aws_cognito_user_pool.necpgame_users.id
}

output "cognito_client_id" {
  description = "Cognito User Pool Client ID"
  value       = aws_cognito_user_pool_client.client.id
}
