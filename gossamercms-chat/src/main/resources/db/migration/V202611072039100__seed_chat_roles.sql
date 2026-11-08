
INSERT INTO permissions (
    id,
    name,
    description,
    "createdAt",
    "updatedAt"
)
VALUES
    (gen_random_uuid(), 'support.chat.access', 'Access the technical support chat interface', now(), now()),
    (gen_random_uuid(), 'support.chat.view', 'View support conversations', now(), now()),
    (gen_random_uuid(), 'support.chat.view_all', 'View all support conversations regardless of assignment', now(), now()),
    (gen_random_uuid(), 'support.chat.create', 'Initiate a support conversation', now(), now()),
    (gen_random_uuid(), 'support.chat.respond', 'Send messages in support conversations', now(), now()),
    (gen_random_uuid(), 'support.chat.assign', 'Assign support conversations to staff members', now(), now()),
    (gen_random_uuid(), 'support.chat.reassign', 'Reassign conversations between support staff', now(), now()),
    (gen_random_uuid(), 'support.chat.close', 'Close support conversations', now(), now()),
    (gen_random_uuid(), 'support.chat.reopen', 'Reopen previously closed support conversations', now(), now()),
    (gen_random_uuid(), 'support.chat.escalate', 'Escalate support conversations to senior support', now(), now()),
    (gen_random_uuid(), 'support.chat.manage', 'Manage support chat configuration and operations', now(), now()),
    (gen_random_uuid(), 'support.chat.view_history', 'View historical support conversations', now(), now()),
    (gen_random_uuid(), 'support.chat.attachments', 'Upload and download support chat attachments', now(), now())
    ON CONFLICT (name) DO NOTHING;


INSERT INTO roles (
    id,
    name,
    description,
    "isSystem",
    "createdAt",
    "updatedAt"
)
VALUES
    ('bfae1105-fd07-4891-bdd8-6ae80151f6a5', 'support-agent', 'Handles assigned technical support conversations', false, now(), now()),
    ('94750f6c-f3f9-4b27-98bc-b8712fc116ab', 'support-supervisor', 'Oversees support agents, manages assignments and escalations', false, now(), now());

INSERT INTO role_permissions (id, "roleId", "permissionId", "createdAt")
SELECT
    gen_random_uuid(),
    r.id,
    p.id,
    now()
FROM roles r
         JOIN permissions p ON TRUE
WHERE r.name = 'support-agent' or r.name = 'support-supervisor';