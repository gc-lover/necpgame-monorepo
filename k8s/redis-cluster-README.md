# Redis Cluster Deployment for NECPGAME
## Issue: #2001 - Redis cluster - data sharding, replication, high availability

This directory contains Kubernetes manifests for deploying a production-ready Redis cluster with the following features:

### 🚀 Features

- **Redis Cluster**: 6-node cluster (3 masters + 3 replicas) with automatic sharding
- **High Availability**: Redis Sentinel for automatic failover and monitoring
- **Security**: Network policies, authentication, and secure configurations
- **Monitoring**: Health checks and metrics endpoints
- **Persistence**: Persistent volumes for data durability
- **Performance**: Optimized for MMOFPS workloads (75k+ concurrent users)

### 📁 File Structure

```
k8s/
├── redis-cluster-statefulset.yaml     # Redis cluster StatefulSet and services
├── redis-cluster-configmap.yaml       # Redis and cluster configuration
├── redis-sentinel-deployment.yaml     # Sentinel deployment for HA
├── redis-sentinel-configmap.yaml      # Sentinel configuration and scripts
├── redis-cluster-secret.yaml          # Authentication secrets
├── redis-cluster-networkpolicy.yaml   # Network security policies
├── redis-cluster-helm/                # Helm chart for deployment
└── redis-cluster-README.md           # This file
```

### 🏗️ Architecture

#### Redis Cluster (6 nodes)
- **3 Master nodes**: Handle writes and coordinate cluster operations
- **3 Replica nodes**: Provide read scalability and failover redundancy
- **Automatic sharding**: Data distributed across 16384 hash slots
- **Cluster bus**: Gossip protocol on port 16379 for node communication

#### Redis Sentinel (3 instances)
- **Monitoring**: Continuously monitor Redis master health
- **Automatic failover**: Promote replicas to masters when needed
- **Client redirection**: Notify clients of master changes
- **Quorum-based decisions**: Require majority consensus for failover

#### Security
- **Authentication**: Password-protected Redis instances
- **Network policies**: Restrict access to authorized services only
- **RBAC**: Kubernetes RBAC for cluster management

### 🚀 Deployment

#### Prerequisites
- Kubernetes 1.19+
- Persistent storage class `fast-ssd` configured
- kubectl configured with cluster access

#### Quick Start

1. **Create namespace** (if not exists):
   ```bash
   kubectl create namespace necpgame
   ```

2. **Deploy Redis cluster**:
   ```bash
   kubectl apply -f redis-cluster-configmap.yaml
   kubectl apply -f redis-cluster-secret.yaml
   kubectl apply -f redis-cluster-statefulset.yaml
   kubectl apply -f redis-sentinel-configmap.yaml
   kubectl apply -f redis-sentinel-deployment.yaml
   kubectl apply -f redis-cluster-networkpolicy.yaml
   ```

3. **Verify deployment**:
   ```bash
   # Check pods
   kubectl get pods -n necpgame -l app=redis-cluster

   # Check cluster status
   kubectl exec -it redis-cluster-0 -n necpgame -- redis-cli cluster nodes

   # Check sentinel status
   kubectl exec -it redis-sentinel-0 -n necpgame -- redis-cli -p 26379 sentinel masters
   ```

#### Using Helm

```bash
# Add helm repo (if applicable)
helm install redis-cluster ./redis-cluster-helm \
  --namespace necpgame \
  --set redis.password=your-secure-password
```

### ⚙️ Configuration

#### Redis Cluster Settings
- **Memory**: 512MB per node (configurable via ConfigMap)
- **Persistence**: AOF + RDB snapshots
- **Cluster timeout**: 15 seconds
- **Migration barrier**: 1 replica minimum

#### Sentinel Settings
- **Quorum**: 2 sentinels required for failover
- **Down-after**: 5 seconds to mark node as down
- **Failover timeout**: 60 seconds maximum failover time

### 🔍 Monitoring

#### Health Checks
```bash
# Check cluster health
kubectl exec -it redis-cluster-0 -n necpgame -- redis-cli cluster info

# Check sentinel health
kubectl exec -it redis-sentinel-0 -n necpgame -- redis-cli -p 26379 sentinel masters
```

#### Metrics
Redis exposes metrics on port 6379. Configure Prometheus to scrape:
```yaml
- job_name: 'redis-cluster'
  static_configs:
    - targets: ['redis-cluster-0.necpgame.svc.cluster.local:6379']
  relabel_configs:
    - source_labels: [__address__]
      regex: '(.*):6379'
      replacement: '${1}:6379'
      target_label: __address__
```

### 🔧 Operations

#### Scaling
```bash
# Scale cluster (manual process - requires careful planning)
# 1. Add new nodes
kubectl scale statefulset redis-cluster --replicas=8 -n necpgame

# 2. Rebalance slots
kubectl exec -it redis-cluster-0 -n necpgame -- redis-cli --cluster reshard <new-node-ip>:6379
```

#### Backup
```bash
# Create backup
kubectl exec redis-cluster-0 -n necpgame -- redis-cli save

# Copy backup
kubectl cp necpgame/redis-cluster-0:/data/dump.rdb ./redis-backup.rdb
```

#### Failover Testing
```bash
# Simulate master failure
kubectl delete pod redis-cluster-0 -n necpgame

# Observe automatic failover
kubectl logs -f redis-sentinel-0 -n necpgame
```

### 🔒 Security

#### Authentication
- Redis password stored in Kubernetes secrets
- Sentinel authenticates with cluster using same password
- Network policies restrict access to authorized pods

#### Network Security
- **Ingress**: Only backend services and API gateway can access Redis
- **Egress**: DNS resolution, monitoring, and cluster communication allowed
- **Isolation**: Separate network policies for cluster and sentinel

### 📊 Performance Tuning

#### Memory Optimization
- **maxmemory**: 512MB per node
- **maxmemory-policy**: allkeys-lru
- **maxmemory-samples**: 5 for better eviction accuracy

#### Connection Pooling
- **tcp-backlog**: 511 connections
- **timeout**: 0 (no idle timeouts)
- **tcp-keepalive**: 300 seconds

#### Cluster Optimization
- **cluster-node-timeout**: 15 seconds
- **cluster-migration-barrier**: 1 replica minimum
- **cluster-require-full-coverage**: no (allows partial cluster operation)

### 🚨 Troubleshooting

#### Common Issues

1. **Cluster not forming**:
   ```bash
   # Check pod logs
   kubectl logs redis-cluster-0 -n necpgame

   # Manual cluster creation
   kubectl exec -it redis-cluster-0 -n necpgame -- redis-cli --cluster create [node-list]
   ```

2. **Sentinel not monitoring**:
   ```bash
   # Check sentinel configuration
   kubectl exec -it redis-sentinel-0 -n necpgame -- cat /etc/redis/sentinel.conf

   # Restart sentinels
   kubectl rollout restart deployment redis-sentinel -n necpgame
   ```

3. **Memory issues**:
   ```bash
   # Check memory usage
   kubectl exec -it redis-cluster-0 -n necpgame -- redis-cli info memory

   # Adjust maxmemory in ConfigMap
   kubectl edit configmap redis-cluster-config -n necpgame
   ```

### 📚 References

- [Redis Cluster Specification](https://redis.io/topics/cluster-spec)
- [Redis Sentinel Documentation](https://redis.io/topics/sentinel)
- [Kubernetes StatefulSets](https://kubernetes.io/docs/concepts/workloads/controllers/statefulset/)

### 🤝 Contributing

When modifying Redis cluster configuration:
1. Update this README
2. Test in staging environment
3. Update Helm chart values
4. Document performance impact

---

**Issue**: #2001 | **Maintainer**: Backend Team | **Version**: 1.0.0