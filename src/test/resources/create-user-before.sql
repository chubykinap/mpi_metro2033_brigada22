delete from alert_messages;
delete from item_in_order;
delete from item;
delete from courier;
delete from delivery_order;
delete from sensor_messages;
delete from movement_sensor;
delete from soldier;
delete from security_post;
delete from soldier_characteristics;
delete from metro_user;

insert into metro_user(enabled, login, password, role) values
(true, 'aaa', '$2a$12$P004s6So6KfTV5wouNElk.uabo0X4dbJUbDteFk4DQM1zK2KkkzZO', 'ADMIN'),
(true, 'ggg', '$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC', 'GENERAL'),
(true, 's1', '$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC', 'SOLDIER'),
(true, 'head_courier', '$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC', 'HEAD_COURIER'),
(true, 'c', '$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC', 'COURIER');



