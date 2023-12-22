INSERT INTO permissions (name, value) VALUES
    ('CREATE_TICKET', true),
    ('READ_TICKET', true),
    ('DELETE_TICKET', true),
    ('CREATE_AWB', true),
    ('READ_AWB', true),
    ('DELETE_AWB', true),
    ('CREATE_ACCOUNT', true),
    ('READ_ACCOUNT', true),
    ('DELETE_ACCOUNT', true),
    ('CREATE_CITY', true),
    ('READ_CITY', true),
    ('DELETE_CITY', true),
    ('CREATE_COUNTRY', true),
    ('READ_COUNTRY', true),
    ('DELETE_COUNTRY', true),
    ('CREATE_DEPARTMENT', true),
    ('READ_DEPARTMENT', true),
    ('DELETE_DEPARTMENT', true),
    ('CREATE_DEPARTMENT_CATEGORY', true),
    ('READ_DEPARTMENT_CATEGORY', true),
    ('DELETE_DEPARTMENT_CATEGORY', true),
    ('CREATE_PRODUCT_TYPE', true),
    ('READ_PRODUCT_TYPE', true),
    ('DELETE_PRODUCT_TYPE', true),
    ('CREATE_SERVICE_TYPE', true),
    ('READ_SERVICE_TYPE', true),
    ('DELETE_SERVICE_TYPE', true),
    ('DASHBOARD', true),
    ('PERMISSION', true),
    ('CREATE_USER', true),
    ('READ_USER', true),
    ('DELETE_USER', true);


INSERT INTO `role_permissions` VALUES
    (1, (SELECT id FROM permissions WHERE name = 'CREATE_TICKET')),
    (1, (SELECT id FROM permissions WHERE name = 'READ_TICKET')),
    (1, (SELECT id FROM permissions WHERE name = 'DELETE_TICKET')),
    (1, (SELECT id FROM permissions WHERE name = 'CREATE_AWB')),
    (1, (SELECT id FROM permissions WHERE name = 'READ_AWB')),
    (1, (SELECT id FROM permissions WHERE name = 'DELETE_AWB')),
    (1, (SELECT id FROM permissions WHERE name = 'CREATE_ACCOUNT')),
    (1, (SELECT id FROM permissions WHERE name = 'READ_ACCOUNT')),
    (1, (SELECT id FROM permissions WHERE name = 'DELETE_ACCOUNT')),
    (1, (SELECT id FROM permissions WHERE name = 'CREATE_CITY')),
    (1, (SELECT id FROM permissions WHERE name = 'READ_CITY')),
    (1, (SELECT id FROM permissions WHERE name = 'DELETE_CITY')),
    (1, (SELECT id FROM permissions WHERE name = 'CREATE_COUNTRY')),
    (1, (SELECT id FROM permissions WHERE name = 'READ_COUNTRY')),
    (1, (SELECT id FROM permissions WHERE name = 'DELETE_COUNTRY')),
    (1, (SELECT id FROM permissions WHERE name = 'CREATE_DEPARTMENT')),
    (1, (SELECT id FROM permissions WHERE name = 'READ_DEPARTMENT')),
    (1, (SELECT id FROM permissions WHERE name = 'DELETE_DEPARTMENT')),
    (1, (SELECT id FROM permissions WHERE name = 'CREATE_DEPARTMENT_CATEGORY')),
    (1, (SELECT id FROM permissions WHERE name = 'READ_DEPARTMENT_CATEGORY')),
    (1, (SELECT id FROM permissions WHERE name = 'DELETE_DEPARTMENT_CATEGORY')),
    (1, (SELECT id FROM permissions WHERE name = 'CREATE_PRODUCT_TYPE')),
    (1, (SELECT id FROM permissions WHERE name = 'READ_PRODUCT_TYPE')),
    (1, (SELECT id FROM permissions WHERE name = 'DELETE_PRODUCT_TYPE')),
    (1, (SELECT id FROM permissions WHERE name = 'CREATE_SERVICE_TYPE')),
    (1, (SELECT id FROM permissions WHERE name = 'READ_SERVICE_TYPE')),
    (1, (SELECT id FROM permissions WHERE name = 'DELETE_SERVICE_TYPE')),
    (1, (SELECT id FROM permissions WHERE name = 'DASHBOARD')),
    (1, (SELECT id FROM permissions WHERE name = 'PERMISSION')),
    (1, (SELECT id FROM permissions WHERE name = 'CREATE_USER')),
    (1, (SELECT id FROM permissions WHERE name = 'READ_USER')),
    (1, (SELECT id FROM permissions WHERE name = 'DELETE_USER'));

INSERT INTO country (name, status)
VALUES
  ('Pakistan', true),
  ('India', true),
  ('Saudi Arabia', true),
  ('United States', true),
  ('United Kingdom', true),
  ('Canada', true),
  ('Australia', true),
  ('Germany', true),
  ('France', true),
  ('Japan', true);


INSERT INTO city (name, status, country_id)
VALUES
  ('Islamabad', true, (SELECT id FROM country WHERE name = 'Pakistan')),
  ('Karachi', true, (SELECT id FROM country WHERE name = 'Pakistan')),
  ('Lahore', true, (SELECT id FROM country WHERE name = 'Pakistan')),

  ('New Delhi', true, (SELECT id FROM country WHERE name = 'India')),
  ('Mumbai', true, (SELECT id FROM country WHERE name = 'India')),
  ('Bangalore', true, (SELECT id FROM country WHERE name = 'India')),

  ('Riyadh', true, (SELECT id FROM country WHERE name = 'Saudi Arabia')),
  ('Jeddah', true, (SELECT id FROM country WHERE name = 'Saudi Arabia')),
  ('Dammam', true, (SELECT id FROM country WHERE name = 'Saudi Arabia')),

  ('New York', true, (SELECT id FROM country WHERE name = 'United States')),
  ('Los Angeles', true, (SELECT id FROM country WHERE name = 'United States')),
  ('Chicago', true, (SELECT id FROM country WHERE name = 'United States')),

  ('London', true, (SELECT id FROM country WHERE name = 'United Kingdom')),
  ('Manchester', true, (SELECT id FROM country WHERE name = 'United Kingdom')),
  ('Birmingham', true, (SELECT id FROM country WHERE name = 'United Kingdom')),

  ('Toronto', true, (SELECT id FROM country WHERE name = 'Canada')),
  ('Vancouver', true, (SELECT id FROM country WHERE name = 'Canada')),
  ('Montreal', true, (SELECT id FROM country WHERE name = 'Canada')),

  ('Sydney', true, (SELECT id FROM country WHERE name = 'Australia')),
  ('Melbourne', true, (SELECT id FROM country WHERE name = 'Australia')),
  ('Brisbane', true, (SELECT id FROM country WHERE name = 'Australia')),

  ('Berlin', true, (SELECT id FROM country WHERE name = 'Germany')),
  ('Munich', true, (SELECT id FROM country WHERE name = 'Germany')),
  ('Hamburg', true, (SELECT id FROM country WHERE name = 'Germany')),

  ('Paris', true, (SELECT id FROM country WHERE name = 'France')),
  ('Marseille', true, (SELECT id FROM country WHERE name = 'France')),
  ('Lyon', true, (SELECT id FROM country WHERE name = 'France')),

  ('Tokyo', true, (SELECT id FROM country WHERE name = 'Japan')),
  ('Osaka', true, (SELECT id FROM country WHERE name = 'Japan')),
  ('Kyoto', true, (SELECT id FROM country WHERE name = 'Japan'));


INSERT INTO department (name, status)
VALUES
  ('Customer Service', true),
  ('Customer Care', true),
  ('Finance', true),
  ('Operation', true),
  ('IT', true),
  ('Admin', true),
  ('HR and Legal', true),
  ('Quality and Security', true);


INSERT INTO department_category (name, status, department_id)
VALUES
  -- Customer Care
  ('Call Back', true, (SELECT id FROM department WHERE name = 'Customer Care')),
  ('Customer Care Case', true, (SELECT id FROM department WHERE name = 'Customer Care')),

  -- Finance
  ('Billing', true, (SELECT id FROM department WHERE name = 'Finance')),
  ('Refund', true, (SELECT id FROM department WHERE name = 'Finance')),
  ('Collection', true, (SELECT id FROM department WHERE name = 'Finance')),

  -- Operation
  ('Urgent Delivery', true, (SELECT id FROM department WHERE name = 'Operation')),
  ('Delivery Complaint', true, (SELECT id FROM department WHERE name = 'Operation')),
  ('Pickup Request', true, (SELECT id FROM department WHERE name = 'Operation')),
  ('Address Update', true, (SELECT id FROM department WHERE name = 'Operation')),
  ('Schedule Delivery', true, (SELECT id FROM department WHERE name = 'Operation')),
  ('Services Request', true, (SELECT id FROM department WHERE name = 'Operation')),

  -- IT
  ('Technical Issue', true, (SELECT id FROM department WHERE name = 'IT')),
  ('Contact Customer', true, (SELECT id FROM department WHERE name = 'IT')),

  -- Admin
  ('Call Back', true, (SELECT id FROM department WHERE name = 'Admin')),

  -- HR & Legal
  ('Call Back', true, (SELECT id FROM department WHERE name = 'HR and Legal')),

  -- Quality & Security
  ('Call Back', true, (SELECT id FROM department WHERE name = 'Quality and Security'));


INSERT INTO product_type (code, name, status)
VALUES
  ('DS', 'Domestic Shipping', true),
  ('IS', 'International Shipping', true),
  ('CDC', 'Customized Delivery Channel', true),
  ('SC', 'Sea Cargo', true);


INSERT INTO service_type (code, name, status, product_type_id)
VALUES
  -- Domestic Shipping
  ('SDPR', 'SALSEL Domestic Priority', true, (SELECT id FROM product_type WHERE code = 'DS')),
  ('SDEX', 'SALSEL Domestic Express', true, (SELECT id FROM product_type WHERE code = 'DS')),
  ('SDLF', 'SALSEL Domestic Loose Freight', true, (SELECT id FROM product_type WHERE code = 'DS')),
  ('SDPF', 'SALSEL Domestic Pallet Freight', true, (SELECT id FROM product_type WHERE code = 'DS')),
  ('SDCS', 'SALSEL Domestic Cargo Shipping', true, (SELECT id FROM product_type WHERE code = 'DS')),

  -- International Shipping
  ('SIPR', 'SALSEL International Priority', true, (SELECT id FROM product_type WHERE code = 'IS')),
  ('SIEX', 'SALSEL International Express', true, (SELECT id FROM product_type WHERE code = 'IS')),
  ('SIPS', 'SALSEL International Promo Service', true, (SELECT id FROM product_type WHERE code = 'IS')),
  ('SIGS', 'SALSEL International Ground Shipping', true, (SELECT id FROM product_type WHERE code = 'IS')),

  -- Customized Delivery Channel
  ('SBDS', 'SALSEL Bullet Delivery Service', true, (SELECT id FROM product_type WHERE code = 'CDC')),
  ('SSDS', 'SALSEL Same Day Service', true, (SELECT id FROM product_type WHERE code = 'CDC')),
  ('SSDE', 'SALSEL Special Delivery Express', true, (SELECT id FROM product_type WHERE code = 'CDC')),

  -- Sea Cargo
  ('SSCD', 'SALSEL Sea Cargo Freight Clearance & Delivery', true, (SELECT id FROM product_type WHERE code = 'SC')),
  ('SSFT', 'SALSEL Sea Cargo Freight Transport', true, (SELECT id FROM product_type WHERE code = 'SC')),
  ('SSFC', 'SALSEL Sea Cargo Freight Clearance', true, (SELECT id FROM product_type WHERE code = 'SC'));


INSERT INTO product_field (name, sequence, status, created_at, type)
VALUES
  ('Category', 1, 'Active', CURRENT_DATE, 'MULTIDROPDOWN'),
  ('Ticket Flag', 2, 'Active', CURRENT_DATE, 'MULTIDROPDOWN'),
  ('Ticket Status', 3, 'Active', CURRENT_DATE, 'MULTIDROPDOWN'),
  ('Currency', 4, 'Active', CURRENT_DATE, 'MULTIDROPDOWN'),
  ('Duty And Tax Billing', 5, 'Active', CURRENT_DATE, 'MULTIDROPDOWN'),
  ('Ticket Type', 6, 'Active', CURRENT_DATE, 'MULTIDROPDOWN'),
  ('Status', 7, 'Active', CURRENT_DATE, 'MULTIDROPDOWN'),
  ('Request Type', 8, 'Active', CURRENT_DATE, 'MULTIDROPDOWN'),
  ('Account Types', 9, 'Active', CURRENT_DATE, 'MULTIDROPDOWN'),
  ('Sales Region', 10, 'Active', CURRENT_DATE, 'MULTIDROPDOWN'),
  ('Sales Agent', 11, 'Active', CURRENT_DATE, 'MULTIDROPDOWN');
  ('Awb Status', 12, 'Active', CURRENT_DATE, 'MULTIDROPDOWN');


INSERT INTO product_field_values (name, status, product_field_id)
VALUES
  ('Complaint', 'Active', (SELECT id FROM product_field WHERE name = 'Category')),
  ('Suggestion', 'Active', (SELECT id FROM product_field WHERE name = 'Category')),
  ('General Inquiry', 'Active', (SELECT id FROM product_field WHERE name = 'Category')),
  ('Service Request', 'Active', (SELECT id FROM product_field WHERE name = 'Category')),

  ('Normal', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Flag')),
  ('Priority', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Flag')),
  ('Urgent', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Flag')),
  ('Extreme Urgent', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Flag')),

  ('Open', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Status')),
  ('Closed', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Status')),
  ('On-Hold', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Status')),
  ('Under Process', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Status')),
  ('Overdue Escalation', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Status')),
  ('Held-FI', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Status')),

  ('SAR', 'Active', (SELECT id FROM product_field WHERE name = 'Currency')),
  ('AED', 'Active', (SELECT id FROM product_field WHERE name = 'Currency')),
  ('BHD', 'Active', (SELECT id FROM product_field WHERE name = 'Currency')),
  ('KWD', 'Active', (SELECT id FROM product_field WHERE name = 'Currency')),
  ('OMR', 'Active', (SELECT id FROM product_field WHERE name = 'Currency')),
  ('SDG', 'Active', (SELECT id FROM product_field WHERE name = 'Currency')),
  ('CNY', 'Active', (SELECT id FROM product_field WHERE name = 'Currency')),
  ('USD', 'Active', (SELECT id FROM product_field WHERE name = 'Currency')),

  ('Bill Shipper', 'Active', (SELECT id FROM product_field WHERE name = 'Duty And Taxes Billing')),
  ('Bill Consignee', 'Active', (SELECT id FROM product_field WHERE name = 'Duty And Taxes Billing')),

  ('Subject', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Type')),
  ('Text Box', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Type')),
  ('Priority', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Type')),
  ('Attachments', 'Active', (SELECT id FROM product_field WHERE name = 'Ticket Type')),

  ('Active', 'Active', (SELECT id FROM product_field WHERE name = 'Status')),
  ('In Active', 'Active', (SELECT id FROM product_field WHERE name = 'Status')),

  ('Pick-up', 'Active', (SELECT id FROM product_field WHERE name = 'Request Type')),
  ('Drop-off', 'Active', (SELECT id FROM product_field WHERE name = 'Request Type')),

  ('Cash', 'Active', (SELECT id FROM product_field WHERE name = 'Account Types')),
  ('Corporate', 'Active', (SELECT id FROM product_field WHERE name = 'Account Types')),
  ('ECOM', 'Active', (SELECT id FROM product_field WHERE name = 'Account Types')),
  ('Freight', 'Active', (SELECT id FROM product_field WHERE name = 'Account Types')),
  ('Cash Retail', 'Active', (SELECT id FROM product_field WHERE name = 'Account Types')),
  ('Freight B2B', 'Active', (SELECT id FROM product_field WHERE name = 'Account Types')),
  ('Freight BCB', 'Active', (SELECT id FROM product_field WHERE name = 'Account Types')),

  ('Saudi Arabia', 'Active', (SELECT id FROM product_field WHERE name = 'Sales Region')),
  ('United Arab Emirates', 'Active', (SELECT id FROM product_field WHERE name = 'Sales Region')),
  ('GCC Other', 'Active', (SELECT id FROM product_field WHERE name = 'Sales Region')),
  ('Sudan', 'Active', (SELECT id FROM product_field WHERE name = 'Sales Region')),
  ('Africa', 'Active', (SELECT id FROM product_field WHERE name = 'Sales Region')),
  ('Turkey', 'Active', (SELECT id FROM product_field WHERE name = 'Sales Region')),
  ('Middle East', 'Active', (SELECT id FROM product_field WHERE name = 'Sales Region')),
  ('United States of America', 'Active', (SELECT id FROM product_field WHERE name = 'Sales Region')),
  ('UK & Europe', 'Active', (SELECT id FROM product_field WHERE name = 'Sales Region')),
  ('China', 'Active', (SELECT id FROM product_field WHERE name = 'Sales Region')),

  ('Mohammad Sameer', 'Active', (SELECT id FROM product_field WHERE name = 'Sales Agent'));

  ('AWB Created', 'Active', (SELECT id FROM product_field WHERE name = 'Awb Status'));
  ('Picked Up', 'Active', (SELECT id FROM product_field WHERE name = 'Awb Status'));
  ('Arrived in Station', 'Active', (SELECT id FROM product_field WHERE name = 'Awb Status'));
  ('Held in Station', 'Active', (SELECT id FROM product_field WHERE name = 'Awb Status'));
  ('Depart from Station', 'Active', (SELECT id FROM product_field WHERE name = 'Awb Status'));
  ('Arrived in Hub', 'Active', (SELECT id FROM product_field WHERE name = 'Awb Status'));
  ('Depart from Hub', 'Active', (SELECT id FROM product_field WHERE name = 'Awb Status'));
  ('Out for Delivery', 'Active', (SELECT id FROM product_field WHERE name = 'Awb Status'));
  ('Delivered', 'Active', (SELECT id FROM product_field WHERE name = 'Awb Status'));


INSERT INTO users VALUES (1,'admin','$2a$12$mHLYj8pQvDPZq1J2hcevUuNYnJT.tzdlZbzokF1n4LBFwpPIxLSoC',true, "info@stepwaysoftwares.com");

INSERT INTO roles
VALUES
    (1,'ROLE_ADMIN'),
    (2,'ROLE_OPERATION_AGENT'),
    (3,'ROLE_CUSTOMER_SERVICE_AGENT'),
    (4,'ROLE_CUSTOMER_CARE_AGENT'),
    (5,'ROLE_MANAGEMENT_USER'),
    (6,'ROLE_OPERATION_USER'),

INSERT INTO user_roles VALUES (1,1);