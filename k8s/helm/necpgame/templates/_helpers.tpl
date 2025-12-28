{{/*
Expand the name of the chart.
*/}}
{{- define "necpgame.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "necpgame.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "necpgame.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "necpgame.labels" -}}
helm.sh/chart: {{ include "necpgame.chart" . }}
{{ include "necpgame.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "necpgame.selectorLabels" -}}
app.kubernetes.io/name: {{ include "necpgame.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "necpgame.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "necpgame.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Database connection string template
*/}}
{{- define "necpgame.databaseUrl" -}}
{{- if .Values.postgresql.enabled }}
{{- printf "postgres://%s:%s@%s-postgresql:5432/%s?sslmode=require" .Values.postgresql.auth.username .Values.postgresql.auth.password .Release.Name .Values.postgresql.auth.database }}
{{- else }}
{{- required "PostgreSQL must be enabled or database URL must be provided" .Values.externalDatabase.url }}
{{- end }}
{{- end }}

{{/*
Redis connection string template
*/}}
{{- define "necpgame.redisUrl" -}}
{{- if .Values.redis.enabled }}
{{- printf "redis://:%s@%s-redis-master:6379/0" .Values.redis.auth.password .Release.Name }}
{{- else }}
{{- required "Redis must be enabled or Redis URL must be provided" .Values.externalRedis.url }}
{{- end }}
{{- end }}

{{/*
Kafka connection string template
*/}}
{{- define "necpgame.kafkaUrl" -}}
{{- if .Values.kafka.enabled }}
{{- printf "%s-kafka:9092" .Release.Name }}
{{- else }}
{{- required "Kafka must be enabled or Kafka URL must be provided" .Values.externalKafka.url }}
{{- end }}
{{- end }}

{{/*
Service template
*/}}
{{- define "necpgame.service" -}}
apiVersion: v1
kind: Service
metadata:
  name: {{ include "necpgame.fullname" . }}-{{ .serviceName }}
  labels:
    {{- include "necpgame.labels" . | nindent 4 }}
    app.kubernetes.io/component: {{ .serviceName }}
spec:
  type: {{ default "ClusterIP" .serviceType }}
  ports:
    - port: {{ default 8080 .servicePort }}
      targetPort: http
      protocol: TCP
      name: http
      {{- if eq .serviceType "NodePort" }}
      nodePort: {{ .nodePort }}
      {{- end }}
  selector:
    {{- include "necpgame.selectorLabels" . | nindent 4 }}
    app.kubernetes.io/component: {{ .serviceName }}
{{- end }}

{{/*
Deployment template
*/}}
{{- define "necpgame.deployment" -}}
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "necpgame.fullname" . }}-{{ .serviceName }}
  labels:
    {{- include "necpgame.labels" . | nindent 4 }}
    app.kubernetes.io/component: {{ .serviceName }}
spec:
  {{- if .hpa }}
  replicas: {{ .Values.hpa.minReplicas }}
  {{- else }}
  replicas: {{ default 1 .replicaCount }}
  {{- end }}
  selector:
    matchLabels:
      {{- include "necpgame.selectorLabels" . | nindent 6 }}
      app.kubernetes.io/component: {{ .serviceName }}
  template:
    metadata:
      labels:
        {{- include "necpgame.selectorLabels" . | nindent 8 }}
        app.kubernetes.io/component: {{ .serviceName }}
      annotations:
        checksum/config: {{ include (print $.Template.BasePath "/configmap.yaml") . | sha256sum }}
    spec:
      {{- with .Values.imagePullSecrets }}
      imagePullSecrets:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      serviceAccountName: {{ include "necpgame.serviceAccountName" . }}
      securityContext:
        {{- toYaml .Values.podSecurityContext | nindent 8 }}
      containers:
        - name: {{ .serviceName }}
          securityContext:
            {{- toYaml .Values.securityContext | nindent 12 }}
          image: "{{ .Values.global.imageRegistry }}/{{ .serviceName }}:{{ .Values.image.tag | default .Chart.AppVersion }}"
          imagePullPolicy: {{ .Values.image.pullPolicy }}
          ports:
            - name: http
              containerPort: {{ default 8080 .containerPort }}
              protocol: TCP
          env:
            {{- include "necpgame.envVars" . | nindent 12 }}
          resources:
            {{- toYaml .resources | nindent 12 }}
          {{- if .livenessProbe }}
          livenessProbe:
            {{- toYaml .livenessProbe | nindent 12 }}
          {{- end }}
          {{- if .readinessProbe }}
          readinessProbe:
            {{- toYaml .readinessProbe | nindent 12 }}
          {{- end }}
      {{- with .Values.nodeSelector }}
      nodeSelector:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      {{- with .Values.affinity }}
      affinity:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      {{- with .Values.tolerations }}
      tolerations:
        {{- toYaml . | nindent 8 }}
      {{- end }}
{{- end }}

{{/*
Environment variables template
*/}}
{{- define "necpgame.envVars" -}}
- name: ENVIRONMENT
  value: {{ .Values.global.environment | quote }}
- name: LOG_LEVEL
  value: {{ .Values.global.logLevel | quote }}
- name: DATABASE_URL
  valueFrom:
    secretKeyRef:
      name: {{ include "necpgame.fullname" . }}-secrets
      key: database-url
- name: REDIS_URL
  valueFrom:
    secretKeyRef:
      name: {{ include "necpgame.fullname" . }}-secrets
      key: redis-url
- name: KAFKA_BROKERS
  valueFrom:
    secretKeyRef:
      name: {{ include "necpgame.fullname" . }}-secrets
      key: kafka-brokers
- name: JWT_SECRET
  valueFrom:
    secretKeyRef:
      name: {{ include "necpgame.fullname" . }}-secrets
      key: jwt-secret
{{- if .serviceName }}
- name: SERVICE_NAME
  value: {{ .serviceName | quote }}
{{- end }}
{{- end }}
