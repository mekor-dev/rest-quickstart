-- User --
INSERT INTO `User` (`id`, `creationDate`, `deleted`, `updateDate`, `email`, `nickname`, `defaultLanguage`, `privacy`, `isAdmin`) VALUES 
(1, '2019-01-24 00:00:00', b'0', '2019-01-24 00:00:00', 'fred.allen@mail.com', 'Fred', 'fr', 'PUBLIC', b'0'), 
(2, '2019-01-24 00:00:00', b'0', '2019-01-24 00:00:00', 'joseph.joubert@mail.com', 'Joseph', 'en', 'PUBLIC', b'0'), 
(3, '2018-01-25 00:00:00', b'0', '2019-01-24 00:00:00', 'francias.bacon@mail.com', 'Francis', 'fr', 'PUBLIC', b'0'), 
(4, '2019-01-27 00:00:00', b'0', '2019-01-24 00:00:00', 'arthur.koestler@mail.com', 'Arthur', 'fr', 'PUBLIC', b'1'), 
(5, '2017-01-05 00:00:00', b'0', '2019-01-24 00:00:00', 'bruce.lee@mail.com', 'Bruce', 'fr', 'PUBLIC', b'0'), 
(6, '2019-04-24 00:00:00', b'0', '2019-01-24 00:00:00', 'hank.aaron@mail.com', 'Hank', 'fr', 'PUBLIC', b'0'), 
(7, '2017-01-24 00:00:00', b'0', '2019-01-24 00:00:00', 'gerald.ford@mail.com', 'Gerald', 'en', 'ALLIANCE', b'0'), 
(8, '2017-01-24 00:00:00', b'1', '2019-01-24 00:00:00', 'alphonse.allais@mail.com', 'Alphonse', 'fr', 'PUBLIC', b'0'),
(9, '2016-01-13 00:00:00', b'0', '2019-01-24 00:00:00', 'adesmond.tutu@mail.com', 'Desmond', NULL, 'PUBLIC', b'0'),
(10, '2019-02-24 00:00:00', b'0', '2019-01-24 00:00:00', 'neil.gaiman@mail.com', 'Neil', 'fr', 'ALLIANCE', b'0'),
(11, '2019-01-13 00:00:00', b'0', '2019-01-24 00:00:00', 'philip.johnson@mail.com', 'Philip', 'fr', 'PUBLIC', b'0'), 
(12, '2020-07-20 00:00:00', b'0', '2019-01-24 00:00:00', 'thomas.malory@mail.com', 'Thomas', 'fr', 'PUBLIC', b'0'), 
(13, '2019-12-07 00:00:00', b'0', '2019-01-24 00:00:00', 'lord@mail.com', 'Lord', 'fr', 'PUBLIC', b'0'), 
(14, '2019-01-08 00:00:00', b'0', '2019-01-24 00:00:00', 'duke.ellington@mail.com', 'Duke', 'fr', 'PUBLIC', b'0'), 
(15, '2019-05-12 00:00:00', b'0', '2019-01-24 00:00:00', 'clarence.darrow@mail.com', 'Clarence', 'en', 'PUBLIC', b'0'), 
(16, '2019-08-19 00:00:00', b'0', '2019-01-24 00:00:00', 'joe.haldeman@mail.com', 'Joe', NULL, 'PUBLIC', b'0'), 
(17, '2013-11-30 00:00:00', b'0', '2019-01-24 00:00:00', 'ann.hannessey@mail.com', 'Ann', 'fr', 'ALLIANCE', b'0'), 
(18, '2013-11-30 00:00:00', b'1', '2019-01-24 00:00:00', 'gene.fowler@mail.com', 'Gene', 'fr', 'PUBLIC', b'0'), 
(19, '2019-02-27 00:00:00', b'0', '2019-01-24 00:00:00', 'daniel.fatiaki@mail.com', 'Daniel', 'fr', 'ALLIANCE', b'0'), 
(20, '2016-10-29 00:00:00', b'0', '2019-01-24 00:00:00', 'ann.landers@mail.com', 'Ann', NULL, 'PUBLIC', b'0'), 
(21, NULL, b'0', '2019-01-24 00:00:00', 'henry.adams@mail.com', 'Henry', 'fr', 'PUBLIC', b'0'), 
(22, '2019-07-05 00:00:00', b'0', '2019-01-24 00:00:00', 'hugh.hewitt@mail.com', 'Hugh', 'fr', 'PUBLIC', b'0'), 
(23, '2019-07-05 00:00:00', b'1', '2019-01-24 00:00:00', 'george.fox@mail.com', 'George', 'fr', 'PUBLIC', b'0'), 
(24, '2017-06-16 00:00:00', b'0', '2019-01-24 00:00:00', 'stan.lee@mail.com', 'Stan', NULL, 'ALLIANCE', b'0'), 
(25, NULL, b'0', '2019-01-24 00:00:00', 'alberto.giacometti@mail.com', 'Alberto', 'fr', 'PUBLIC', b'0'), 
(26, NULL, b'0', '2019-01-24 00:00:00', 'paul@mail.com', 'Paul', 'fr', 'PUBLIC', b'0'), 
(27, '2019-01-19 00:00:00', b'0', '2019-01-24 00:00:00', 'eugene.ionesco@mail.com', 'Eugene', 'fr', 'PUBLIC', b'0'), 
(28, '2019-05-11 00:00:00', b'0', '2019-01-24 00:00:00', 'thomas.overbury@mail.com', 'Thomas', 'fr', 'PUBLIC', b'0'),
(29, '2019-05-11 00:00:00', b'0', '2019-01-24 00:00:00', 'unmec@mail.com', 'Gerome', 'fr', 'PUBLIC', b'0');

-- FileEntity --
INSERT INTO `FileEntity` (`id`, `creationDate`, `deleted`, `updateDate`, `path`, `type`, `uploaderID`) VALUES 
(1, '2019-02-26 00:00:00', b'0', '2019-02-26 00:00:00', '/hero_avatar.png', 'IMAGE', 1),
(2, '2019-02-26 00:00:00', b'0', '2019-02-26 00:00:00', '/user.png', 'IMAGE', 15),
(3, '2019-02-26 00:00:00', b'0', '2019-02-26 00:00:00', '/avatar.png', 'IMAGE', 1);

-- FileTag --
INSERT INTO `FileTag` (`id`, `creationDate`, `deleted`, `updateDate`, `value`, `fileID`) VALUES 
(1, '2019-02-26 00:00:00', b'0', '2019-02-26 00:00:00', 'tag1', '1'), 
(2, '2019-02-26 00:00:00', b'0', '2019-02-26 00:00:00', 'tag2', '1'),
(3, '2019-02-26 00:00:00', b'0', '2019-02-26 00:00:00', 'tag3', '2'),
(4, '2019-02-26 00:00:00', b'0', '2019-02-26 00:00:00', 'tag4', '3');

-- PasswordCredentials (everybody have 'passwordTest' as pass) --
INSERT INTO `PasswordCredentials` (`id`, `creationDate`, `deleted`, `updateDate`, `password`, `userID`) VALUES 
(1, '2019-01-30 00:00:00', b'0', '2019-01-30 00:00:00', 'eded82fead402293e0ad6f774aa4cb1d77245c2bc62f3b4c4f3dfcaacb336fe2', '1'),
(2, '2019-01-30 00:00:00', b'0', '2019-01-30 00:00:00', 'eded82fead402293e0ad6f774aa4cb1d77245c2bc62f3b4c4f3dfcaacb336fe2', '2'),
(3, '2019-01-30 00:00:00', b'0', '2019-01-30 00:00:00', 'eded82fead402293e0ad6f774aa4cb1d77245c2bc62f3b4c4f3dfcaacb336fe2', '3'),
(4, '2019-01-30 00:00:00', b'0', '2019-01-30 00:00:00', 'eded82fead402293e0ad6f774aa4cb1d77245c2bc62f3b4c4f3dfcaacb336fe2', '19');

-- ForgottenPasswordToken
INSERT INTO `ForgottenPasswordToken` (`id`, `creationDate`, `deleted`, `updateDate`, `expirationDate`, `token`, `userID`) VALUES 
(1, '2019-10-26 00:00:00', b'0', '2019-10-26 00:00:00', now() + INTERVAL 1 DAY, 'f8760e68-9b6f-4816-8c7e-b9a451fcbd2d', '1'), 
(2, '2019-10-26 00:00:00', b'0', '2019-10-26 00:00:00', now() - INTERVAL 1 DAY, 'b23ca5ac-3b91-4334-b355-92b0915c93e5', '2'), 
(3, '2019-10-26 00:00:00', b'0', '2019-10-26 00:00:00', now() - INTERVAL 1 DAY, 'replace Service', '3'),
(4, '2019-10-26 00:00:00', b'0', '2019-10-26 00:00:00', now() - INTERVAL 1 DAY, 'replace API', '7'),
(5, '2019-10-26 00:00:00', b'0', '2019-10-26 00:00:00', now() + INTERVAL 1 DAY, 'Change password API', '19');

-- Hero --
INSERT INTO `Hero` (`id`, `creationDate`, `deleted`, `updateDate`, `avatarID`, `name`) VALUES 
(1, '2020-06-15 00:00:00', b'0', '2020-06-15 00:00:00', '1', '{\"fr\":\"L''homme araignée\", \"en\":\"Spiderman\"}'),
(2, '2020-06-15 00:00:00', b'0', '2020-06-15 00:00:00', '3', '{\"fr\":\"Lanterne verte\", \"en\":\"Green Lantern\"}'),
(3, '2020-06-15 00:00:00', b'0', '2020-06-15 00:00:00', '1', '{\"fr\":\"Capitaine Amérique\", \"en\":\"Captain America\"}'),
(4, '2020-06-15 00:00:00', b'0', '2020-06-15 00:00:00', '1', '{\"fr\":\"L''homme araignée 2\", \"en\":\"Spiderman 2\"}');

-- UserHero --
INSERT INTO `UserHero` (`heroID`, `userID`, `creationDate`, `deleted`, `updateDate`, `power`) VALUES 
(1, 1, '2020-06-15 00:00:00', b'0', '2020-06-15 00:00:00', '1000'), 
(2, 1, '2020-06-15 00:00:00', b'0', '2020-06-15 00:00:00', '1500'), 
(3, 1, '2020-06-15 00:00:00', b'0', '2020-06-15 00:00:00', '500'), 
(3, 2, '2020-06-15 00:00:00', b'0', '2020-06-15 00:00:00', NULL), 
(1, 2, '2020-06-15 00:00:00', b'0', '2020-06-15 00:00:00', '3000');

INSERT INTO `MailMessage` (`mailType`, `creationDate`, `deleted`, `updateDate`, `subject`) VALUES 
('FORGOTTEN_PASSWORD', '2019-10-24 00:00:00', b'0', '2019-10-24 00:00:00', '{\"fr\":\"Mot de passe oublié\", \"en\":\"Forgotten password\"}');

INSERT INTO `MailMessageHTML` (`messageType`, `html`, `language`) VALUES
('FORGOTTEN_PASSWORD', '<div>Hello {userName}</div>\r\n<div>Forgotten password</div>\r\n<div>Link: {forgotten_password_link}</div>', 'en'),
('FORGOTTEN_PASSWORD', '<div>Bonjour {userName}</div>\r\n<div>Mot de passe oublié</div>\r\n<div>Lien: {forgotten_password_link}</div>', 'fr');

