-- Insert reservations that overlap (100, 200)
INSERT INTO reservation(`id`, `start_date`, `number_of_guests`, `end_date`, `status`, `accommodation_id`, `guest_id`,
                        `total_price`)
VALUES (1, 50, 4, 150, 1, 1, 3, 220.00),
       (2, 150, 1, 175, 1, 1, 3, 220.00),
       (3, 150, 1, 300, 1, 1, 3, 220.00);