-- Create table for project documentation (YAML files from knowledge/)
-- This table stores all project documentation, designs, mechanics, and specifications

CREATE TABLE IF NOT EXISTS project.documentation (
    id BIGSERIAL PRIMARY KEY,
    doc_id VARCHAR(255) NOT NULL UNIQUE, -- from metadata.id
    title TEXT, -- from metadata.title
    document_type VARCHAR(100), -- from metadata.document_type (mechanics, implementation, design, etc.)
    category VARCHAR(100), -- from metadata.category
    status VARCHAR(50), -- from metadata.status
    version VARCHAR(20), -- from metadata.version
    last_updated TIMESTAMP WITH TIME ZONE, -- from metadata.last_updated
    concept_approved BOOLEAN DEFAULT FALSE, -- from metadata.concept_approved
    concept_reviewed_at TIMESTAMP WITH TIME ZONE, -- from metadata.concept_reviewed_at
    owners JSONB, -- from metadata.owners (array of owner objects)
    tags TEXT[], -- from metadata.tags (array of strings)
    topics TEXT[], -- from metadata.topics (array of strings)
    related_systems TEXT[], -- from metadata.related_systems (array of strings)
    related_documents JSONB, -- from metadata.related_documents (array of document relations)
    source TEXT, -- from metadata.source
    visibility VARCHAR(50), -- from metadata.visibility
    audience TEXT[], -- from metadata.audience (array of strings)
    risk_level VARCHAR(20), -- from metadata.risk_level
    content JSONB, -- Full content of the YAML file (summary, content sections, etc.)
    metadata JSONB, -- Complete metadata section as JSON
    source_file TEXT NOT NULL, -- Relative path to source YAML file
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_documentation_doc_id ON project.documentation(doc_id);
CREATE INDEX IF NOT EXISTS idx_documentation_document_type ON project.documentation(document_type);
CREATE INDEX IF NOT EXISTS idx_documentation_category ON project.documentation(category);
CREATE INDEX IF NOT EXISTS idx_documentation_status ON project.documentation(status);
CREATE INDEX IF NOT EXISTS idx_documentation_tags ON project.documentation USING GIN(tags);
CREATE INDEX IF NOT EXISTS idx_documentation_topics ON project.documentation USING GIN(topics);
CREATE INDEX IF NOT EXISTS idx_documentation_related_systems ON project.documentation USING GIN(related_systems);
CREATE INDEX IF NOT EXISTS idx_documentation_content ON project.documentation USING GIN(content);
CREATE INDEX IF NOT EXISTS idx_documentation_source_file ON project.documentation(source_file);

-- Comments
COMMENT ON TABLE project.documentation IS 'Project documentation from knowledge/ YAML files';
COMMENT ON COLUMN project.documentation.doc_id IS 'Unique document identifier from metadata.id';
COMMENT ON COLUMN project.documentation.document_type IS 'Type of document (mechanics, implementation, design, etc.)';
COMMENT ON COLUMN project.documentation.category IS 'Document category within its type';
COMMENT ON COLUMN project.documentation.content IS 'Full content of the document as JSONB';
COMMENT ON COLUMN project.documentation.metadata IS 'Complete metadata section as JSONB';
COMMENT ON COLUMN project.documentation.source_file IS 'Relative path to source YAML file';
