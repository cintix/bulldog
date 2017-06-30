/**
 * Author:  migo
 * Created: 29-06-2017
 */



CREATE TABLE client (
	id serial primary key, 
	name varchar(100) not null, 
	created_at timestamp without time zone not null default now(),
	description text	
);	 

CREATE UNIQUE INDEX client_name_idx ON client (lower(name));

CREATE TABLE client_mapping (
	id serial primary key, 
	client_id int references client(id) not null, 
        actions int not null default 0,
	name varchar(100) not null, 
	path varchar not null, 
	url varchar not null, 
	pattern varchar(100) not null default '*.*'	,
	created_at timestamp without time zone not null default now(),
	updated_at timestamp without time zone not null default now()	
);	 

CREATE INDEX client_id_idx ON client_mapping (client_id);	
CREATE UNIQUE INDEX client_mapping_id_path_idx ON client_mapping (client_id, path);

CREATE TABLE client_response (
	client_mapping_id int references client_mapping(id), 
	client_id int references client(id),
	filename varchar not null, 
	created_at timestamp without time zone not null default now(),
	response_code int not null,
	response text
);

CREATE INDEX client_response_id_idx ON client_response (client_id);		 
CREATE INDEX client_response_client_mapping_id_idx ON client_response (client_mapping_id);	
CREATE UNIQUE INDEX client_response_id_file_idx ON client_response (client_id, filename);

CREATE VIEW response as 
SELECT      c.id as client_id,
            c.name as client_name,
            cm.id as mapping_id,
            cm.name as mapping_name,
            cr.filename as filename,
            cr.response_code as response_code,
            cr.response as response
FROM 		client_response cr
LEFT JOIN 	client_mapping cm on (cr.client_mapping_id = cm.id)
LEFT JOIN   client c on (cr.client_id = c.id);	