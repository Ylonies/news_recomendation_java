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
                                        url text,
                                        is_basic boolean
);

INSERT INTO websites (name, url, is_basic)
VALUES ('INFOQ', 'https://www.infoq.com/development', true),
       ('THREE_D', 'https://3dnews.ru', true),
       ('HI_TECH', 'https://hi-tech.mail.ru/news/', true);

CREATE TABLE IF NOT EXISTS catalogs (
                                        catalog_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                                        name text NOT NULL,
                                        is_basic boolean
);

INSERT INTO catalogs (name, is_basic)
VALUES ('DevOps', true),
       ('Frontend', true),
       ('Backend', true),
       ('Data Science', true),
       ('Machine Learning', true),
       ('Cybersecurity', true),
       ('Cloud Computing', true),
       ('Mobile Development', true),
       ('Game Development', true),
       ('Databases', true);

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
                                            name text,
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
