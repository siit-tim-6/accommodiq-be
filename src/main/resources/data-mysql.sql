-- Insert sample data into `user` table
INSERT INTO `user` (`id`, `address`, `first_name`, `last_name`, `phone_number`)
VALUES (4, 'address 1', 'Admin', 'Admin', '555-9101'),
       (1, '123 Main St', 'John', 'Doe', '555-1234'),
       (2, '456 Oak St', 'Jane', 'Smith', '555-5678'),
       (3, '789 Pine St', 'Bob', 'Johnson', '555-9101');

-- Insert sample data into `account` table
INSERT INTO `account` (`id`, `email`, `password`, `role`, `status`, `user_id`, `activation_expires`)
VALUES (4, 'admin', "$2a$10$gcj5DROzl4O6T3l08ygBjOGZeIJOnRidmlIWaQEivSYKMu67ldIaC", 2, 1, 4, 1672531200),
       (1, 'john.doe@example.com', "$2a$10$gcj5DROzl4O6T3l08ygBjOGZeIJOnRidmlIWaQEivSYKMu67ldIaC", 1, 1, 1, 1672531200),
       (2, 'jane.smith@example.com', "$2a$10$gcj5DROzl4O6T3l08ygBjOGZeIJOnRidmlIWaQEivSYKMu67ldIaC", 1, 1, 2,
        1672531200),
       (3, 'guest.bj@example.com', "$2a$10$gcj5DROzl4O6T3l08ygBjOGZeIJOnRidmlIWaQEivSYKMu67ldIaC", 0, 1, 3, 1672531200);

-- Insert sample data into `host` table
INSERT INTO `host` (`id`)
VALUES (1),
       (2);

INSERT INTO `guest` (`id`)
VALUES (3);

-- Insert sample data into 'guest' table
INSERT INTO `guest` (`id`)
VALUES (3);

-- Insert sample data into `availability` table
INSERT INTO `availability` (`id`, `from_date`, `price`, `to_date`)
VALUES (1, 1672531200, 100.00, 1672617600),
       (2, 1672617600, 150.00, 1672704000),
       (3, 1672704000, 120.00, 1672790400),
       (4, 1704067200, 50.00, 1704585600),
       (5, 1706745600, 100.00, 1707264000),
       (6, 1709251200, 120.00, 1709769600);

-- Insert sample data into `accommodation` table
INSERT INTO `accommodation` (`id`, `status`, `automatic_acceptance`, `cancellation_deadline`, `description`,
                             `max_guests`, `min_guests`, `pricing_type`, `title`, `type`, `host_id`, `address`,
                             `latitude`, `longitude`)
VALUES (1, 1, 1, 48, 'Cozy apartment near downtown', 4, 2, 0, 'Downtown Retreat', 'Apartment', 1,
        'Svetozara Miletica 23, 21203 Novi Sad, Serbia', 45.2588948, 19.76116795),
       (2, 1, 0, 72, 'Spacious house with a garden', 8, 4, 1, 'Green Haven', 'House', 2,
        'Trg republike 13, 21101 Novi Sad, Serbia', 45.2578895, 19.850468576804),
       (3, 0, 1, 24, 'Charming cottage by the lake', 2, 1, 0, 'Lake View Cottage', 'Cottage', 1,
        'Gunduliceva 24, 21101 Novi Sad, Serbia', 45.263714, 19.8470915);

-- Insert sample data into `accommodation_available` table
INSERT INTO `accommodation_available` (`accommodation_id`, `available_id`)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (1, 4),
       (2, 5),
       (3, 6);

-- Insert sample data into `review` table
INSERT INTO `review` (`id`, `comment`, `date`, `rating`, `status`, `guest_id`)
VALUES (1, 'Great place!', 1672617600, 5, 1, 3),
       (2, 'Comfortable stay', 1672704000, 4, 0, 3),
       (3, 'Beautiful location', 1672790400, 5, 0, 3),
       (4, 'Amazing place!', 1672617600, 5, 2, 3),
       (5, 'Pleasant guy', 1672704000, 4, 1, 3),
       (6, 'Very cool landowner', 1672790400, 5, 0, 3);

-- Insert sample data into `accommodation_reviews` table
INSERT INTO `accommodation_reviews` (`accommodation_id`, `reviews_id`)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 4);

-- Insert sample data into `host_reviews` table
INSERT INTO `host_reviews` (`host_id`, `reviews_id`)
VALUES (1, 5),
       (2, 6);

-- Insert sample data into `report` table
INSERT INTO `report` (`id`, `reason`, `timestamp`, `reported_user_id`, `reporting_user_id`)
VALUES (1, 'Inappropriate content', 1672531200, 3, 1),
       (2, 'Late check-in', 1672617600, 3, 2),
       (3, 'Noise complaint', 1672704000, 1, 3);

-- Insert sample data into `reservation` table
INSERT INTO `reservation` (`id`, `end_date`, `number_of_guests`, `start_date`, `status`, `accommodation_id`, `user_id`)
VALUES
    (1, 1672617600, 2, 1672531200, 2, 1, 3),
    (2, 1672704000, 4, 1672617600, 1, 2, 3),
    (3, 1672790400, 1, 1672704000, 0, 3, 3);

DELETE
FROM `reservation`;
DELETE
FROM `report`;
DELETE
FROM `host_reviews`;
DELETE
FROM `accommodation_reviews`;
DELETE
FROM `review`;
DELETE
FROM `accommodation_available`;
DELETE
FROM `accommodation_images`;
DELETE
FROM `accommodation`;
DELETE
FROM `availability`;
DELETE
FROM `host`;
DELETE
FROM `account`;
DELETE
FROM `user`;
