delete from courier;
delete from metro_user;

insert into metro_user(enabled, login, password, role) values
(true, 'head_courier', '$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC', 'HEAD_COURIER');



