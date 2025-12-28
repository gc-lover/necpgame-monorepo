# API Gateway Configuration for NECPGAME Serverless
# Enterprise-grade API Gateway with rate limiting and authentication

# API Gateway Deployment
resource "aws_api_gateway_deployment" "necpgame_prod" {
  depends_on = [
    aws_api_gateway_integration.achievement_lambda,
    aws_api_gateway_integration.quest_lambda,
    aws_api_gateway_integration.inventory_lambda,
    aws_api_gateway_integration.economy_lambda,
    aws_api_gateway_integration.combat_stats_lambda,
    aws_api_gateway_integration.analytics_lambda,
  ]

  rest_api_id = aws_api_gateway_rest_api.necpgame_api.id
  stage_name  = var.api_gateway_config.stage_name

  variables = {
    deployed_at = timestamp()
  }

  lifecycle {
    create_before_destroy = true
  }
}

# API Gateway Stage Configuration
resource "aws_api_gateway_stage" "prod" {
  deployment_id = aws_api_gateway_deployment.necpgame_prod.id
  rest_api_id   = aws_api_gateway_rest_api.necpgame_api.id
  stage_name    = var.api_gateway_config.stage_name
}

# API Gateway Method Settings for Performance
resource "aws_api_gateway_method_settings" "all" {
  rest_api_id = aws_api_gateway_rest_api.necpgame_api.id
  stage_name  = aws_api_gateway_stage.prod.stage_name
  method_path = "*/*"

  settings {
    throttling_burst_limit = var.api_gateway_config.throttling_settings.burst_limit
    throttling_rate_limit  = var.api_gateway_config.throttling_settings.rate_limit

    # Enable CloudWatch metrics and logging
    metrics_enabled = true
    logging_level   = "INFO"
    data_trace_enabled = true
  }
}

# API Gateway Usage Plan
resource "aws_api_gateway_usage_plan" "necpgame_plan" {
  name         = "necpgame-serverless-usage-plan"
  description  = "Usage plan for NECPGAME serverless API"

  api_stages {
    api_id = aws_api_gateway_rest_api.necpgame_api.id
    stage  = aws_api_gateway_stage.prod.stage_name
  }

  throttle_settings {
    burst_limit = 100
    rate_limit  = 50
  }

  quota_settings {
    limit  = 10000
    offset = 0
    period = "DAY"
  }
}

# API Key for Usage Plan
resource "aws_api_gateway_api_key" "necpgame_key" {
  name        = "necpgame-serverless-api-key"
  description = "API key for NECPGAME serverless services"
  enabled     = true
}

resource "aws_api_gateway_usage_plan_key" "main" {
  key_id        = aws_api_gateway_api_key.necpgame_key.id
  key_type      = "API_KEY"
  usage_plan_id = aws_api_gateway_usage_plan.necpgame_plan.id
}

# Additional API Gateway Resources
resource "aws_api_gateway_resource" "quest" {
  rest_api_id = aws_api_gateway_rest_api.necpgame_api.id
  parent_id   = aws_api_gateway_rest_api.necpgame_api.root_resource_id
  path_part   = "quest"
}

resource "aws_api_gateway_resource" "inventory" {
  rest_api_id = aws_api_gateway_rest_api.necpgame_api.id
  parent_id   = aws_api_gateway_rest_api.necpgame_api.root_resource_id
  path_part   = "inventory"
}

resource "aws_api_gateway_resource" "economy" {
  rest_api_id = aws_api_gateway_rest_api.necpgame_api.id
  parent_id   = aws_api_gateway_rest_api.necpgame_api.root_resource_id
  path_part   = "economy"
}

resource "aws_api_gateway_resource" "combat" {
  rest_api_id = aws_api_gateway_rest_api.necpgame_api.id
  parent_id   = aws_api_gateway_rest_api.necpgame_api.root_resource_id
  path_part   = "combat"
}

resource "aws_api_gateway_resource" "analytics" {
  rest_api_id = aws_api_gateway_rest_api.necpgame_api.id
  parent_id   = aws_api_gateway_rest_api.necpgame_api.root_resource_id
  path_part   = "analytics"
}

# HTTP Methods for each service
resource "aws_api_gateway_method" "quest_post" {
  rest_api_id   = aws_api_gateway_rest_api.necpgame_api.id
  resource_id   = aws_api_gateway_resource.quest.id
  http_method   = "POST"
  authorization = "COGNITO_USER_POOLS"
  authorizer_id = aws_api_gateway_authorizer.cognito.id
  api_key_required = true
}

resource "aws_api_gateway_method" "inventory_post" {
  rest_api_id   = aws_api_gateway_rest_api.necpgame_api.id
  resource_id   = aws_api_gateway_resource.inventory.id
  http_method   = "POST"
  authorization = "COGNITO_USER_POOLS"
  authorizer_id = aws_api_gateway_authorizer.cognito.id
  api_key_required = true
}

resource "aws_api_gateway_method" "economy_post" {
  rest_api_id   = aws_api_gateway_rest_api.necpgame_api.id
  resource_id   = aws_api_gateway_resource.economy.id
  http_method   = "POST"
  authorization = "COGNITO_USER_POOLS"
  authorizer_id = aws_api_gateway_authorizer.cognito.id
  api_key_required = true
}

resource "aws_api_gateway_method" "combat_post" {
  rest_api_id   = aws_api_gateway_rest_api.necpgame_api.id
  resource_id   = aws_api_gateway_resource.combat.id
  http_method   = "POST"
  authorization = "COGNITO_USER_POOLS"
  authorizer_id = aws_api_gateway_authorizer.cognito.id
  api_key_required = true
}

resource "aws_api_gateway_method" "analytics_post" {
  rest_api_id   = aws_api_gateway_rest_api.necpgame_api.id
  resource_id   = aws_api_gateway_resource.analytics.id
  http_method   = "POST"
  authorization = "COGNITO_USER_POOLS"
  authorizer_id = aws_api_gateway_authorizer.cognito.id
  api_key_required = true
}

# Lambda Integrations
resource "aws_api_gateway_integration" "quest_lambda" {
  rest_api_id             = aws_api_gateway_rest_api.necpgame_api.id
  resource_id             = aws_api_gateway_resource.quest.id
  http_method             = aws_api_gateway_method.quest_post.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = module.lambda_functions["quest-service"].invoke_arn
}

resource "aws_api_gateway_integration" "inventory_lambda" {
  rest_api_id             = aws_api_gateway_rest_api.necpgame_api.id
  resource_id             = aws_api_gateway_resource.inventory.id
  http_method             = aws_api_gateway_method.inventory_post.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = module.lambda_functions["inventory-service"].invoke_arn
}

resource "aws_api_gateway_integration" "economy_lambda" {
  rest_api_id             = aws_api_gateway_rest_api.necpgame_api.id
  resource_id             = aws_api_gateway_resource.economy.id
  http_method             = aws_api_gateway_method.economy_post.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = module.lambda_functions["economy-service"].invoke_arn
}

resource "aws_api_gateway_integration" "combat_stats_lambda" {
  rest_api_id             = aws_api_gateway_rest_api.necpgame_api.id
  resource_id             = aws_api_gateway_resource.combat.id
  http_method             = aws_api_gateway_method.combat_post.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = module.lambda_functions["combat-stats-service"].invoke_arn
}

resource "aws_api_gateway_integration" "analytics_lambda" {
  rest_api_id             = aws_api_gateway_rest_api.necpgame_api.id
  resource_id             = aws_api_gateway_resource.analytics.id
  http_method             = aws_api_gateway_method.analytics_post.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = module.lambda_functions["analytics-service"].invoke_arn
}

# Lambda Permissions
resource "aws_lambda_permission" "api_gateway_quest" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = module.lambda_functions["quest-service"].function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_api_gateway_rest_api.necpgame_api.execution_arn}/*/*/*"
}

resource "aws_lambda_permission" "api_gateway_inventory" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = module.lambda_functions["inventory-service"].function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_api_gateway_rest_api.necpgame_api.execution_arn}/*/*/*"
}

resource "aws_lambda_permission" "api_gateway_economy" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = module.lambda_functions["economy-service"].function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_api_gateway_rest_api.necpgame_api.execution_arn}/*/*/*"
}

resource "aws_lambda_permission" "api_gateway_combat_stats" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = module.lambda_functions["combat-stats-service"].function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_api_gateway_rest_api.necpgame_api.execution_arn}/*/*/*"
}

resource "aws_lambda_permission" "api_gateway_analytics" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = module.lambda_functions["analytics-service"].function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_api_gateway_rest_api.necpgame_api.execution_arn}/*/*/*"
}

# Custom Domain for API Gateway (optional)
resource "aws_api_gateway_domain_name" "api" {
  count = var.create_custom_domain ? 1 : 0

  domain_name     = var.api_domain_name
  certificate_arn = aws_acm_certificate.api[0].arn
}

resource "aws_api_gateway_base_path_mapping" "api" {
  count = var.create_custom_domain ? 1 : 0

  api_id      = aws_api_gateway_rest_api.necpgame_api.id
  stage_name  = aws_api_gateway_stage.prod.stage_name
  domain_name = aws_api_gateway_domain_name.api[0].domain_name
}

# ACM Certificate for Custom Domain (optional)
resource "aws_acm_certificate" "api" {
  count = var.create_custom_domain ? 1 : 0

  domain_name       = var.api_domain_name
  validation_method = "DNS"

  tags = var.tags
}

# WAF for API Protection
resource "aws_wafv2_web_acl" "api_waf" {
  name        = "necpgame-api-waf"
  description = "WAF for NECPGAME API Gateway protection"
  scope       = "REGIONAL"

  default_action {
    allow {}
  }

  # Rate limiting rule
  rule {
    name     = "rate-limit"
    priority = 1

    action {
      block {}
    }

    statement {
      rate_based_statement {
        limit              = 1000
        aggregate_key_type = "IP"
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "rate-limit"
      sampled_requests_enabled   = true
    }
  }

  visibility_config {
    cloudwatch_metrics_enabled = true
    metric_name                = "necpgame-api-waf"
    sampled_requests_enabled   = true
  }
}

resource "aws_wafv2_web_acl_association" "api_gateway" {
  resource_arn = aws_api_gateway_stage.prod.arn
  web_acl_arn  = aws_wafv2_web_acl.api_waf.arn
}
