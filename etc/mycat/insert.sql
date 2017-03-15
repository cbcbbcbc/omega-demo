use sk_dev;

/*!mycat:dataNode=dn1*/ create function getTaskZoneCode() returns char(2) deterministic return('00');
/*!mycat:dataNode=dn2*/ create function getTaskZoneCode() returns char(2) deterministic return('01');

insert into User
(id, name, zoneCode)
values
('User01', 'User01', '01');

insert into User
(id, name, zoneCode)
values
('User02', 'User02', '02');

insert into User
(id, name, zoneCode)
values
('User03', 'User03', '03');

insert into User
(id, name, zoneCode)
values
('User04', 'User04', '04');

insert into User
(id, name, zoneCode)
values
('User05', 'User05', '05');

insert into User
(id, name, zoneCode)
values
('User06', 'User06', '06');

insert into User
(id, name, zoneCode)
values
('User07', 'User07', '07');

insert into User
(id, name, zoneCode)
values
('User08', 'User08', '08');

insert into User
(id, name, zoneCode)
values
('User09', 'User09', '09');