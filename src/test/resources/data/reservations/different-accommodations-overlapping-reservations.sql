-- Insert reservations that not overlap (100, 200)
INSERT INTO reservation(`id`, `start_date`, `number_of_guests`, `end_date`, `status`, `accommodation_id`, `guest_id`,
                        `total_price`)
VALUES (1, 200, 1, 300, 1, 1, 3, 220.00),
       (2, 50, 1, 100, 1, 1, 3, 220.00),
       (3, 150, 1, 250, 1, 2, 3, 220.00);