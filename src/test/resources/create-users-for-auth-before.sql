delete from metro_user;

insert into metro_user(enabled, login, password, role) values
(true, 'a', '$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC', 'ADMIN'),
(true, 'gen', '$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC', 'GENERAL'),
(true, 's', '$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC', 'SOLDIER'),
(true, 'he', '$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC', 'HEAD_ENGINEER'),
(true, 'e', '$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC', 'ENGINEER'),
(true, 'hc', '$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC', 'HEAD_COURIER'),
(true, 'c', '$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC', 'COURIER'),
(true, 'gue', '$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC', 'GUEST');

