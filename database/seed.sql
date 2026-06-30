-- seed.sql
-- Sample seed data for Babua DSA Patterns Sheet 2025
-- Note: Replace this with the full list of patterns and problems once provided.

INSERT INTO public.patterns (id, name, sort_order) VALUES
(1, 'Two Pointers', 1),
(2, 'Sliding Window', 2),
(3, 'Fast & Slow Pointers', 3),
(4, 'Binary Search', 4);

-- Restart sequence if needed
SELECT setval('patterns_id_seq', 4);

INSERT INTO public.problems (pattern_id, number, title, difficulty, leetcode_url, video_url, sort_order) VALUES
-- Two Pointers
(1, 88, 'Merge Sorted Array', 'Easy', 'https://leetcode.com/problems/merge-sorted-array/', NULL, 1),
(1, 125, 'Valid Palindrome', 'Easy', 'https://leetcode.com/problems/valid-palindrome/', NULL, 2),
(1, 15, '3Sum', 'Medium', 'https://leetcode.com/problems/3sum/', NULL, 3),
(1, 19, 'Remove Nth Node From End of List', 'Medium', 'https://leetcode.com/problems/remove-nth-node-from-end-of-list/', NULL, 4),
(1, 75, 'Sort Colors', 'Medium', 'https://leetcode.com/problems/sort-colors/', NULL, 5),

-- Sliding Window
(2, 643, 'Maximum Average Subarray I', 'Easy', 'https://leetcode.com/problems/maximum-average-subarray-i/', NULL, 1),
(2, 3, 'Longest Substring Without Repeating Characters', 'Medium', 'https://leetcode.com/problems/longest-substring-without-repeating-characters/', NULL, 2);
