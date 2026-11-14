
insert into ticket_types (id, name, description) values
            ('1e7f8c7e-1c2b-4d3a-3f8e-1a2b3c4d5e6f', 'General Admission', 'Standard entry ticket for the event.'),
            ('00000000-0000-4000-8000-000000000002', 'VIP', 'Exclusive VIP access with additional perks.'),
            ('00000000-0000-4000-8000-000000000003', 'Student', 'Discounted ticket for students with valid ID.'),
            ('00000000-0000-4000-8000-000000000004', 'Early Bird', 'Discounted ticket for students with valid ID.'),
            ('00000000-0000-4000-8000-000000000005', 'Complimentary', 'Free admission.'),
            ('00000000-0000-4000-8000-000000000006', 'Reserved Seating', 'Assigned seating.');



INSERT INTO event_categories (
    id,
    name,
    slug,
    description,
    "parentId",
    "sortOrder",
    "isActive",
    "deletedAt",
    "createdAt",
    "updatedAt"
)
VALUES
-- Arts & Culture
('10000000-0000-4000-7000-000000000001', 'Arts & Culture', 'arts-culture', 'Arts, culture, creative expression and cultural experiences.', NULL, 1, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-7000-000000000002', 'Visual Arts', 'visual-arts', 'Art exhibitions, galleries, painting, sculpture and photography.', '10000000-0000-4000-7000-000000000001', 1, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-7000-000000000003', 'Performing Arts', 'performing-arts', 'Theatre, dance, opera and live performance.', '10000000-0000-4000-7000-000000000001', 2, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-7000-000000000004', 'Film & Media', 'film-media', 'Film screenings, media events and audiovisual experiences.', '10000000-0000-4000-7000-000000000001', 3, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-7000-000000000005', 'Cultural Festivals', 'cultural-festivals', 'Cultural celebrations, heritage events and community traditions.', '10000000-0000-4000-7000-000000000001', 4, TRUE, NULL, NOW(), NOW()),

-- Business & Professional
('10000000-0000-4000-7000-000000000006', 'Business & Professional', 'business-professional', 'Business networking, professional development and industry events.', NULL, 2, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-7000-000000000007', 'Networking', 'networking', 'Professional networking, meetups and business connections.', '10000000-0000-4000-7000-000000000006', 1, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-6000-000000000006', 'Conferences', 'conferences', 'Industry conferences, summits and professional gatherings.', '10000000-0000-4000-7000-000000000006', 2, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-6000-000000000017', 'Entrepreneurship', 'entrepreneurship', 'Startup events, founder meetups and business innovation.', '10000000-0000-4000-7000-000000000006', 3, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-6000-000000000010', 'Career & Job Fairs', 'career-job-fairs', 'Career development, recruitment and employment events.', '10000000-0000-4000-7000-000000000006', 4, TRUE, NULL, NOW(), NOW()),

-- Community & Lifestyle
('10000000-0000-4000-5000-000000000011', 'Community & Lifestyle', 'community-lifestyle', 'Community gatherings, lifestyle activities and social events.', NULL, 3, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-5000-000000000012', 'Community Events', 'community-events', 'Local gatherings, neighbourhood events and community activities.', '10000000-0000-4000-5000-000000000011', 1, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-5000-000000000013', 'Family & Kids', 'family-kids', 'Family-friendly activities, children’s events and entertainment.', '10000000-0000-4000-5000-000000000011', 2, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000014', 'Food & Drink', 'food-drink', 'Food festivals, tastings, culinary experiences and dining events.', '10000000-0000-4000-5000-000000000011', 3, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000015', 'Home & Garden', 'home-garden', 'Gardening, home improvement, design and decorating events.', '10000000-0000-4000-5000-000000000011', 4, TRUE, NULL, NOW(), NOW()),

-- Education
('10000000-0000-4000-8000-000000000016', 'Education & Learning', 'education-learning', 'Educational activities, learning opportunities and workshops.', NULL, 4, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000017', 'Workshops', 'workshops', 'Hands-on workshops, practical training and skill development.', '10000000-0000-4000-8000-000000000016', 1, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000018', 'Technology', 'technology', 'Technology events, software, IT and emerging technologies.', '10000000-0000-4000-8000-000000000016', 2, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000117', 'Science', 'science', 'Science talks, research presentations and discovery events.', '10000000-0000-4000-8000-000000000016', 3, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000020', 'Personal Development', 'personal-development', 'Personal growth, leadership and self-improvement events.', '10000000-0000-4000-8000-000000000016', 4, TRUE, NULL, NOW(), NOW()),

-- Entertainment
('10000000-0000-4000-8000-000000000021', 'Entertainment', 'entertainment', 'Live entertainment, performances and recreational events.', NULL, 5, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000022', 'Concerts & Music', 'concerts-music', 'Live music, concerts, gigs and musical performances.', '10000000-0000-4000-8000-000000000021', 1, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000023', 'Comedy', 'comedy', 'Stand-up comedy, improv and humorous performances.', '10000000-0000-4000-8000-000000000021', 2, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000024', 'Nightlife', 'nightlife', 'Parties, nightlife experiences and social entertainment.', '10000000-0000-4000-8000-000000000021', 3, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000025', 'Festivals', 'festivals', 'Music festivals, seasonal celebrations and public festivals.', '10000000-0000-4000-8000-000000000021', 4, TRUE, NULL, NOW(), NOW()),

-- Health & Wellness
('10000000-0000-4000-8000-000000000026', 'Health & Wellness', 'health-wellness', 'Health, wellbeing, fitness and mindfulness events.', NULL, 6, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000027', 'Fitness', 'fitness', 'Fitness classes, exercise events and physical training.', '10000000-0000-4000-8000-000000000026', 1, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000028', 'Yoga & Meditation', 'yoga-meditation', 'Yoga classes, meditation sessions and mindfulness experiences.', '10000000-0000-4000-8000-000000000026', 2, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000127', 'Mental Wellness', 'mental-wellness', 'Mental wellbeing, stress management and wellness education.', '10000000-0000-4000-8000-000000000026', 3, TRUE, NULL, NOW(), NOW()),

-- Hobbies & Special Interests
('10000000-0000-4000-8000-000000000030', 'Hobbies & Special Interests', 'hobbies-special-interests', 'Hobbies, enthusiast groups and recreational interests.', NULL, 7, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000031', 'Books & Literature', 'books-literature', 'Book clubs, readings, author events and literary gatherings.', '10000000-0000-4000-8000-000000000030', 1, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000032', 'Gaming', 'gaming', 'Video games, board games, esports and gaming meetups.', '10000000-0000-4000-8000-000000000030', 2, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000033', 'Photography', 'photography', 'Photography classes, photo walks and photography meetups.', '10000000-0000-4000-8000-000000000030', 3, TRUE, NULL, NOW(), NOW()),

-- Sports & Recreation
('10000000-0000-4000-8000-000000000034', 'Sports & Recreation', 'sports-recreation', 'Sports competitions, recreational activities and outdoor events.', NULL, 8, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000035', 'Team Sports', 'team-sports', 'Team sporting events, tournaments and competitions.', '10000000-0000-4000-8000-000000000034', 1, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000036', 'Running', 'running', 'Running races, marathons, fun runs and running clubs.', '10000000-0000-4000-8000-000000000034', 2, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000037', 'Outdoor & Adventure', 'outdoor-adventure', 'Hiking, camping, outdoor recreation and adventure activities.', '10000000-0000-4000-8000-000000000034', 3, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000038', 'Motorsports', 'motorsports', 'Motorsport events, racing and automotive gatherings.', '10000000-0000-4000-8000-000000000034', 4, TRUE, NULL, NOW(), NOW()),

-- Charity & Causes
('10000000-0000-4000-8000-000000000039', 'Charity & Causes', 'charity-causes', 'Fundraising, charitable activities and social impact events.', NULL, 9, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000040', 'Fundraisers', 'fundraisers', 'Fundraising events, benefit dinners and charity auctions.', '10000000-0000-4000-8000-000000000039', 1, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000041', 'Volunteering', 'volunteering', 'Volunteer opportunities and community service activities.', '10000000-0000-4000-8000-000000000039', 2, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000042', 'Environmental Causes', 'environmental-causes', 'Environmental awareness, conservation and sustainability events.', '10000000-0000-4000-8000-000000000039', 3, TRUE, NULL, NOW(), NOW()),

-- Travel & Tourism
('10000000-0000-4000-8000-000000000043', 'Travel & Tourism', 'travel-tourism', 'Travel experiences, tourism activities and destination events.', NULL, 10, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000044', 'Tours & Sightseeing', 'tours-sightseeing', 'Guided tours, sightseeing and local exploration.', '10000000-0000-4000-8000-000000000043', 1, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000045', 'Food Tours', 'food-tours', 'Culinary tours, tasting experiences and food discoveries.', '10000000-0000-4000-8000-000000000043', 2, TRUE, NULL, NOW(), NOW()),

-- Fashion & Shopping
('10000000-0000-4000-8000-000000000046', 'Fashion & Shopping', 'fashion-shopping', 'Fashion shows, shopping events, markets and retail experiences.', NULL, 11, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000047', 'Markets & Fairs', 'markets-fairs', 'Craft markets, flea markets, trade fairs and vendor events.', '10000000-0000-4000-8000-000000000046', 1, TRUE, NULL, NOW(), NOW()),
('10000000-0000-4000-8000-000000000048', 'Fashion Shows', 'fashion-shows', 'Fashion exhibitions, runway shows and designer events.', '10000000-0000-4000-8000-000000000046', 2, TRUE, NULL, NOW(), NOW())

    ON CONFLICT (id) DO NOTHING;



