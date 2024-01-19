-- Insert not accepted reservations
-- Insert reservations that overlap (100, 200)
INSERT INTO reservation(
    `id`, `start_date`, `number_of_guests`, `end_date`, `status`, `accommodation_id`, `guest_id`
)
VALUES
    (1, 50, 4, 150, 0, 1, 3),
    (2, 150, 1, 175, 2, 1, 3),
    (3, 150, 1, 300, 3, 1, 3);