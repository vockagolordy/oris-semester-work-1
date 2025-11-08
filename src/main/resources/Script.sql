-- Таблица сессий

CREATE TABLE sessions (
                          session_id VARCHAR(50) PRIMARY KEY,
                          user_id INT REFERENCES users(id) ON DELETE CASCADE,
                          expire_at TIMESTAMP NOT NULL
);

--  Таблица пользователей

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       balance NUMERIC(12,2) DEFAULT 0,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Добавление полей для безопасного хранения паролей

ALTER TABLE users
    ADD COLUMN password_hash VARCHAR(255) NOT NULL,
    ADD COLUMN salt VARCHAR(100) NOT NULL;
ALTER TABLE users DROP COLUMN password; --  Удаление старого поля password (после миграции данных)

--  Таблица групп

CREATE TABLE groups (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        description TEXT,
                        created_by INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--  Таблица участников групп

CREATE TABLE group_members (
                               id SERIAL PRIMARY KEY,
                               user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               group_id INT NOT NULL REFERENCES groups(id) ON DELETE CASCADE,
                               role VARCHAR(50) DEFAULT 'member',
                               joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               UNIQUE (user_id, group_id)
);

--  Таблица целей

CREATE TABLE goals (
                       id SERIAL PRIMARY KEY,
                       group_id INT REFERENCES groups(id) ON DELETE CASCADE,
                       name VARCHAR(100) NOT NULL,
                       target_amount NUMERIC(12,2) NOT NULL,
                       current_amount NUMERIC(12,2) DEFAULT 0,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Корректировка: у пользователя без группы тоже может быть цель

ALTER TABLE goals ADD COLUMN user_id INT REFERENCES users(id) ON DELETE CASCADE;

-- Проверка, что хотя бы одно поле заполнено

ALTER TABLE goals
    ADD CONSTRAINT chk_goal_owner CHECK (
        (group_id IS NOT NULL AND user_id IS NULL) OR
        (group_id IS NULL AND user_id IS NOT NULL)
        );

--  Таблица взносов участников

CREATE TABLE contributions (
                               id SERIAL PRIMARY KEY,
                               goal_id INT NOT NULL REFERENCES goals(id) ON DELETE CASCADE,
                               user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               amount NUMERIC(12,2) NOT NULL CHECK (amount > 0),
                               contributed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--  Таблица расходов / доходов

CREATE TABLE expenses (
                          id SERIAL PRIMARY KEY,
                          user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          type VARCHAR(10) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
                          category VARCHAR(100),
                          amount NUMERIC(12,2) NOT NULL CHECK (amount > 0),
                          description TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--  Триггер: обновление суммы цели при добавлении нового взноса

CREATE OR REPLACE FUNCTION update_goal_progress()
    RETURNS TRIGGER AS $$
BEGIN
UPDATE goals
SET current_amount = current_amount + NEW.amount
WHERE id = NEW.goal_id;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_goal_progress
    AFTER INSERT ON contributions
    FOR EACH ROW
    EXECUTE FUNCTION update_goal_progress();

--  Тестовые данные

-- Пользователи
INSERT INTO users (name, email, password_hash, salt, balance)
VALUES
    ('Alice', 'alice@mail.com', 'jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=', 'salt_alice_123', 1200.00),
    ('Bob', 'bob@mail.com', '9wLXkNeVQ1AAzrNyUvFty8J2Fr3Rk2x6LQ7W6pW4T2E=', 'salt_bob_456', 800.00),
    ('Charlie', 'charlie@mail.com', 'v9qP6W2nX8rT1mK7yL4pB0cR3dF5hJ8wE2aN1gM6zQ=', 'salt_charlie_789', 500.00);

-- Группы
INSERT INTO groups (name, description, created_by)
VALUES
    ('Friends Fund', 'Общие расходы компании друзей', 1),
    ('Trip Fund', 'Сбор денег на поездку', 1);

-- Участники групп
INSERT INTO group_members (user_id, group_id, role)
VALUES
    (1, 1, 'creator'),
    (2, 1, 'member'),
    (3, 1, 'member'),
    (1, 2, 'creator'),
    (2, 2, 'member');

-- Цели
INSERT INTO goals (group_id, user_id, name, target_amount, current_amount)
VALUES
    (1, NULL, 'Новый проектор', 10000, 2500),
    (2, NULL, 'Поездка в горы', 5000, 1200),
    (NULL, 1, 'Новый ноутбук', 1500, 300),
    (NULL, 2, 'Велосипед', 800, 150);

-- Взносы участников
INSERT INTO contributions (goal_id, user_id, amount)
VALUES
    (1, 1, 1000),
    (1, 2, 1500),
    (2, 2, 1200),
    (3, 1, 300),
    (4, 2, 150);

-- Расходы и доходы пользователей
INSERT INTO expenses (user_id, type, category, amount, description)
VALUES
    (1, 'EXPENSE', 'Food', 500.00, 'Pizza for team'),
    (2, 'EXPENSE', 'Transport', 200.00, 'Taxi to meeting'),
    (3, 'EXPENSE', 'Equipment', 150.00, 'Tent for trip'),
    (1, 'INCOME', 'Salary', 2000.00, 'October salary'),
    (2, 'INCOME', 'Freelance', 1000.00, 'Design project');