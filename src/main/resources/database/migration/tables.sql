CREATE TABLE IF NOT EXISTS articles (
                                        article_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                                        name text NOT NULL,
                                        description text NOT NULL,
                                        date text NOT NULL,
                                        link text NOT NULL,
                                        creation_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS websites (
                                        website_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                                        name text NOT NULL,
                                        url text
);

INSERT INTO websites (website_id, name, url)
VALUES ('00000000-0000-0000-0000-000000000000', 'INFOQ', 'https://www.infoq.com/development'),
       ('00000000-0000-0000-0000-000000000001', 'THREE_D', 'https://3dnews.ru'),
       ('00000000-0000-0000-0000-000000000002', 'HI_TECH', 'https://hi-tech.mail.ru/news/');

CREATE TABLE IF NOT EXISTS catalogs (
                                        catalog_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                                        name text NOT NULL
);

INSERT INTO catalogs (catalog_id, name)
VALUES ('00000000-0000-0000-0000-000000000000', 'DevOps'),
       ('00000000-0000-0000-0000-000000000001', 'Frontend'),
       ('00000000-0000-0000-0000-000000000002', 'Backend'),
       ('00000000-0000-0000-0000-000000000003', 'Data Science'),
       ('00000000-0000-0000-0000-000000000004', 'Machine Learning'),
       ('00000000-0000-0000-0000-000000000005', 'Cybersecurity'),
       ('00000000-0000-0000-0000-000000000006', 'Cloud Computing'),
       ('00000000-0000-0000-0000-000000000007', 'Mobile Development'),
       ('00000000-0000-0000-0000-000000000008', 'Game Development'),
       ('00000000-0000-0000-0000-000000000009', 'Databases');

CREATE TABLE IF NOT EXISTS users (
                                     user_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                                     password text NOT NULL,
                                     name text NOT NULL
);

CREATE TABLE IF NOT EXISTS user_time (
                                         user_id UUID NOT NULL,
                                         time timestamp NOT NULL,
                                         PRIMARY KEY (user_id),
                                         CONSTRAINT user_time_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS article_category (
                                                article_id UUID NOT NULL,
                                                catalog_id UUID NOT NULL,
                                                website_id UUID NOT NULL,
                                                PRIMARY KEY (article_id, catalog_id, website_id),
                                                CONSTRAINT article_category_article_id_fk FOREIGN KEY (article_id) REFERENCES articles (article_id),
                                                CONSTRAINT article_category_catalog_id_fk FOREIGN KEY (catalog_id) REFERENCES catalogs (catalog_id),
                                                CONSTRAINT article_category_website_id_fk FOREIGN KEY (website_id) REFERENCES websites (website_id)
);

CREATE TABLE IF NOT EXISTS user_catalog (
                                            user_id UUID NOT NULL,
                                            catalog_id UUID NOT NULL,
                                            PRIMARY KEY (user_id, catalog_id),
                                            CONSTRAINT user_catalog_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id),
                                            CONSTRAINT user_catalog_catalog_id_fk FOREIGN KEY (catalog_id) REFERENCES catalogs (catalog_id)
);

CREATE TABLE IF NOT EXISTS user_website (
                                            website_id UUID,
                                            user_id UUID NOT NULL,
                                            url text,
                                            name text NOT NULL,
                                            PRIMARY KEY (website_id, user_id),
                                            CONSTRAINT user_website_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id),
                                            CONSTRAINT user_website_website_id_fk FOREIGN KEY (website_id) REFERENCES websites (website_id) ON DELETE CASCADE
);
