-- Insert sample data into `user` table
INSERT INTO user(`id`, `address`, `first_name`, `last_name`, `phone_number`)
VALUES (1, '123 Main St', 'John', 'Doe', '555-1234'),
       (3, '789 Pine St', 'Bob', 'Johnson', '555-9101');

-- Insert sample data into `account` table
INSERT INTO account(`id`, `email`, `password`, `role`, `status`, `user_id`, `activation_expires`)
VALUES (1, 'john.doe@example.com', '$2a$10$gcj5DROzl4O6T3l08ygBjOGZeIJOnRidmlIWaQEivSYKMu67ldIaC', 1, 1, 1, 1672531200000),
       (3, 'guest.bj@example.com', '$2a$10$gcj5DROzl4O6T3l08ygBjOGZeIJOnRidmlIWaQEivSYKMu67ldIaC', 0, 1, 3, 1672531200000);

-- Insert sample data into `host` table
INSERT INTO `host` (`id`)
VALUES (1);

INSERT INTO `guest` (`id`)
VALUES (3);

-- Accommodation entity
INSERT INTO accommodation(
    `id`, `status`, `automatic_acceptance`, `cancellation_deadline`, `description`,
    `max_guests`, `min_guests`, `pricing_type`, `title`, `type`, `host_id`, `address`,
    `latitude`, `longitude`
)
VALUES (
           1, 1, 1, 2, 'Cozy apartment near downtown', 4, 2, 0, 'Downtown Retreat', 'Apartment', 1,
           'Svetozara Miletica 23, 21203 Novi Sad, Serbia', 45.2588948, 19.76116795
       ),
       (
           2, 1, 0, 3, 'Spacious house with a garden', 8, 4, 1, 'Green Haven', 'House', 1,
           'Trg republike 13, 21101 Novi Sad, Serbia', 45.2578895, 19.850468576804
       ),
       (
           3, 0, 1, 1, 'Charming cottage by the lake', 2, 1, 0, 'Lake View Cottage', 'Cottage', 1,
           'Gunduliceva 24, 21101 Novi Sad, Serbia', 45.263714, 19.8470915
       );

-- Availability entity
INSERT INTO availability(`id`, `from_date`, `price`, `to_date`)
VALUES (1, 0, 100.00, 3000000000000),
       (2, 0, 150.00, 3000000000000),
       (3, 0, 120.00, 3000000000000);

-- Accommodation_Available entity (Many-to-Many relationship)
INSERT INTO accommodation_available(`accommodation_id`, `available_id`)
VALUES (1, 1),
       (2, 2),
       (3, 3);




