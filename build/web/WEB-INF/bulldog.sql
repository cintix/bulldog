/**
 * Author:  migo
 * Created: 29-06-2017
 */



CREATE TABLE client (
	id serial primary key, 
	name varchar(100) not null, 
	path varchar not null, 
	created_at timestamp without time zone not null default now(),
	description text	
);	 

CREATE TABLE client_mapping (
	id serial primary key, 
	client_id int references client(id) not null, 
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
	