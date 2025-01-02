CREATE TABLE images (
    instance_id VARCHAR(64) PRIMARY KEY,
    series_id VARCHAR(64) NOT NULL,
    study_id VARCHAR(64) NOT NULL,
    patient_id VARCHAR(64) NOT NULL,
    modality VARCHAR(16) NOT NULL,
    image_type VARCHAR(32),
    instance_number INTEGER,
    storage_path VARCHAR(255) NOT NULL,
    file_size BIGINT,
    width INTEGER,
    height INTEGER,
    bits_allocated INTEGER,
    transfer_syntax VARCHAR(64),
    acquisition_datetime TIMESTAMP,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_images_series_id ON images(series_id);
CREATE INDEX idx_images_study_id ON images(study_id);
CREATE INDEX idx_images_patient_id ON images(patient_id);

CREATE TABLE image_upload_tasks (
    upload_id VARCHAR(36) PRIMARY KEY,
    instance_id VARCHAR(64),
    status VARCHAR(32) NOT NULL,
    message TEXT,
    progress INTEGER,
    file_path VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(255)
);

CREATE INDEX idx_upload_tasks_status ON image_upload_tasks(status);
CREATE INDEX idx_upload_tasks_instance_id ON image_upload_tasks(instance_id); 