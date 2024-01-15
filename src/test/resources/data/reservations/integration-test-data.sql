-- Insert sample data into `user` table
INSERT INTO `user` (`id`, `address`, `first_name`, `last_name`, `phone_number`)
VALUES (4, 'address 1', 'Admin', 'Admin', '555-9101'),
       (1, '123 Main St', 'John', 'Doe', '555-1234'),
       (2, '456 Oak St', 'Jane', 'Smith', '555-5678'),
       (3, '789 Pine St', 'Bob', 'Johnson', '555-9101');

-- Insert sample data into `account` table
INSERT INTO `account` (`id`, `email`, `password`, `role`, `status`, `user_id`, `activation_expires`)
VALUES (4, 'admin', '$2a$10$gcj5DROzl4O6T3l08ygBjOGZeIJOnRidmlIWaQEivSYKMu67ldIaC', 2, 1, 4, 1672531200000),
       (1, 'john.doe@example.com', '$2a$10$gcj5DROzl4O6T3l08ygBjOGZeIJOnRidmlIWaQEivSYKMu67ldIaC', 1, 1, 1, 1672531200000),
       (2, 'jane.smith@example.com', '$2a$10$gcj5DROzl4O6T3l08ygBjOGZeIJOnRidmlIWaQEivSYKMu67ldIaC', 1, 1, 2,
        1672531200000),
       (3, 'guest.bj@example.com', '$2a$10$gcj5DROzl4O6T3l08ygBjOGZeIJOnRidmlIWaQEivSYKMu67ldIaC', 0, 1, 3, 1672531200000);

-- Insert sample data into `host` table
INSERT INTO `host` (`id`)
VALUES (1),
       (2);

INSERT INTO `guest` (`id`)
VALUES (3);

-- Insert sample data into `availability` table
INSERT INTO `availability` (`id`, `from_date`, `price`, `to_date`)
VALUES (1, 1672531200000, 100.00, 1672617600000),
       (2, 1672617600000, 150.00, 1672704000000),
       (3, 1672704000000, 120.00, 1672790400000),
       (4, 1704067200000, 50.00, 1704585600000),
       (5, 1706745600000, 100.00, 1707264000000),
       (6, 1709251200000, 120.00, 1709769600000);

-- Insert sample data into `accommodation` table
INSERT INTO `accommodation` (`id`, `status`, `automatic_acceptance`, `cancellation_deadline`, `description`,
                             `max_guests`, `min_guests`, `pricing_type`, `title`, `type`, `host_id`, `address`,
                             `latitude`, `longitude`)
VALUES (1, 1, 1, 2, 'Cozy apartment near downtown', 4, 2, 0, 'Downtown Retreat', 'Apartment', 1,
        'Svetozara Miletica 23, 21203 Novi Sad, Serbia', 45.2588948, 19.76116795),
       (2, 1, 0, 3, 'Spacious house with a garden', 8, 4, 1, 'Green Haven', 'House', 2,
        'Trg republike 13, 21101 Novi Sad, Serbia', 45.2578895, 19.850468576804),
       (3, 0, 1, 1, 'Charming cottage by the lake', 2, 1, 0, 'Lake View Cottage', 'Cottage', 1,
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
VALUES (1, 'Inappropriate content', 1672531200000, 3, 1),
       (2, 'Late check-in', 1672617600000, 3, 2),
       (3, 'Noise complaint', 1672704000000, 1, 3);

-- Insert sample data into `reservation` table
INSERT INTO `reservation` (`id`, `end_date`, `number_of_guests`, `start_date`, `status`, `accommodation_id`, `guest_id`)
VALUES (1, 1672617600000, 2, 1672531200000, 2, 1, 3),
       (2, 1672704000000, 4, 1672617600000, 1, 2, 3),
       (3, 1672790400000, 1, 1672704000000, 0, 3, 3);

INSERT INTO `notification_setting` (`id`, `is_on`, `type`, `user_id`)
VALUES
    (1, true, 0, 1),
    (2, true, 1, 1),
    (3, true, 2, 1),
    (4, true, 3, 1),
    (5, true, 0, 2),
    (6, true, 1, 2),
    (7, true, 2, 2),
    (8, true, 3, 2),
    (9, true, 4, 3);

INSERT INTO `notification` (`id`, `text`, `time`, `seen`, `type`, `user_id`)
VALUES
    (1, 'New Reservation request', 1704150472000, false, 0, 1),
    (2, 'Reservation have been canceled', 1704150472000, false, 1, 1),
    (3, 'Guest rated you', 1704150472000, false, 2, 1),
    (4, 'Guest rated your accommodation', 1704150472000, false, 3, 1);