#!/bin/bash
# NECPGAME Serverless Infrastructure Deployment Script
# Enterprise-grade AWS deployment automation

set -e

# Configuration
STACK_NAME="${STACK_NAME:-necpgame-serverless}"
STAGE="${STAGE:-dev}"
REGION="${REGION:-us-east-1}"
PROFILE="${AWS_PROFILE:-default}"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    log_info "Checking prerequisites..."

    # Check AWS CLI
    if ! command -v aws &> /dev/null; then
        log_error "AWS CLI is not installed. Please install it first."
        exit 1
    fi

    # Check SAM CLI
    if ! command -v sam &> /dev/null; then
        log_error "AWS SAM CLI is not installed. Please install it first."
        exit 1
    fi

    # Check Docker
    if ! command -v docker &> /dev/null; then
        log_error "Docker is not installed. Please install it first."
        exit 1
    fi

    log_success "Prerequisites check passed"
}

# Build Lambda functions
build_functions() {
    log_info "Building Lambda functions..."

    # Build Go functions
    services=(
        "services/auth-service-go"
        "services/realtime-combat-service-go"
        "services/tournament-spectator-service-go"
        "services/game-analytics-dashboard-service-go"
        "services/dynamic-quests-service-go"
        "services/webrtc-signaling-service-go"
        "services/event-processing-service-go"
        "services/ws-lobby-go"
    )

    for service in "${services[@]}"; do
        if [ -d "$service" ]; then
            log_info "Building $service..."
            cd "$service"
            GOOS=linux GOARCH=amd64 CGO_ENABLED=0 go build -o main main.go
            cd - > /dev/null
        else
            log_warning "Service $service not found, skipping..."
        fi
    done

    log_success "Lambda functions built successfully"
}

# Validate CloudFormation template
validate_template() {
    log_info "Validating CloudFormation template..."

    aws cloudformation validate-template \
        --template-body file://infrastructure/serverless/template.yaml \
        --region $REGION \
        --profile $PROFILE

    log_success "CloudFormation template validation passed"
}

# Deploy to AWS
deploy_stack() {
    log_info "Deploying serverless stack to AWS..."

    # Create S3 bucket for deployment artifacts
    BUCKET_NAME="${STACK_NAME}-artifacts-$(date +%s)"

    aws s3 mb "s3://${BUCKET_NAME}" --region $REGION --profile $PROFILE

    # Package and deploy
    sam package \
        --template-file infrastructure/serverless/template.yaml \
        --output-template-file packaged.yaml \
        --s3-bucket $BUCKET_NAME \
        --region $REGION \
        --profile $PROFILE

    sam deploy \
        --template-file packaged.yaml \
        --stack-name $STACK_NAME \
        --capabilities CAPABILITY_IAM \
        --region $REGION \
        --profile $PROFILE \
        --parameter-overrides Stage=$STAGE \
        --tags Project=NECPGAME Environment=$STAGE

    # Clean up
    aws s3 rb "s3://${BUCKET_NAME}" --force --region $REGION --profile $PROFILE
    rm -f packaged.yaml

    log_success "Serverless stack deployed successfully"
}

# Get stack outputs
get_outputs() {
    log_info "Getting stack outputs..."

    aws cloudformation describe-stacks \
        --stack-name $STACK_NAME \
        --region $REGION \
        --profile $PROFILE \
        --query 'Stacks[0].Outputs' \
        --output table
}

# Run tests
run_tests() {
    log_info "Running deployment tests..."

    # Get API Gateway URL
    API_URL=$(aws cloudformation describe-stacks \
        --stack-name $STACK_NAME \
        --region $REGION \
        --profile $PROFILE \
        --query 'Stacks[0].Outputs[?OutputKey==`ApiGatewayUrl`].OutputValue' \
        --output text)

    if [ -z "$API_URL" ]; then
        log_error "Failed to get API Gateway URL"
        return 1
    fi

    log_info "API Gateway URL: $API_URL"

    # Test health endpoint
    if curl -s -f "$API_URL/health" > /dev/null; then
        log_success "Health check passed"
    else
        log_error "Health check failed"
        return 1
    fi

    # Test WebSocket endpoint
    WS_URL=$(aws cloudformation describe-stacks \
        --stack-name $STACK_NAME \
        --region $REGION \
        --profile $PROFILE \
        --query 'Stacks[0].Outputs[?OutputKey==`WebSocketApiUrl`].OutputValue' \
        --output text)

    if [ -n "$WS_URL" ]; then
        log_info "WebSocket URL: $WS_URL"
        log_success "WebSocket endpoint configured"
    else
        log_warning "WebSocket URL not found"
    fi
}

# Main deployment flow
main() {
    log_info "Starting NECPGAME Serverless Deployment"
    log_info "Stack: $STACK_NAME"
    log_info "Stage: $STAGE"
    log_info "Region: $REGION"

    check_prerequisites
    build_functions
    validate_template
    deploy_stack
    get_outputs
    run_tests

    log_success "NECPGAME Serverless Deployment Completed!"
    log_info "Your MMOFPS RPG is now running on AWS Lambda!"
}

# Handle command line arguments
case "${1:-}" in
    "validate")
        check_prerequisites
        validate_template
        ;;
    "build")
        check_prerequisites
        build_functions
        ;;
    "deploy")
        main
        ;;
    "test")
        run_tests
        ;;
    "outputs")
        get_outputs
        ;;
    *)
        echo "Usage: $0 [validate|build|deploy|test|outputs]"
        echo ""
        echo "Commands:"
        echo "  validate  - Validate CloudFormation template"
        echo "  build     - Build Lambda functions"
        echo "  deploy    - Full deployment (default)"
        echo "  test      - Run deployment tests"
        echo "  outputs   - Show stack outputs"
        echo ""
        echo "Environment variables:"
        echo "  STACK_NAME     - CloudFormation stack name (default: necpgame-serverless)"
        echo "  STAGE          - Deployment stage (default: dev)"
        echo "  REGION         - AWS region (default: us-east-1)"
        echo "  AWS_PROFILE    - AWS profile (default: default)"
        exit 1
        ;;
esac
