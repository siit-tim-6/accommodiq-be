-- Insert reservations that not overlap (100, 200)
INSERT INTO reservation(`id`, `start_date`, `number_of_guests`, `end_date`, `status`, `accommodation_id`, `guest_id`,
                        `total_price`)
VALUES (5, 250, 1, 300, 1, 1, 3, 220.00),
       (6, 50, 1, 75, 1, 1, 3, 220.00);