use crate::solutions::{*};

pub struct Day02 {

}

struct Range {
    min: u64,
    max: u64
}

struct IdRange {
    min: u64,
    max: u64,
    value: u64,
    add_factor: u64,
    exclude: Vec<u64>
}

impl Day02 {

    pub fn new_boxed() -> Box<Self> {
        Box::new(Day02 {})
    }

}

impl Day for Day02 {

    fn day_number(&self) -> u32 {
        return 2;
    }

    fn solve_part_1(&self, lines: &Vec<String>) -> String {
        let line = format!("{}{}", lines.get(0).unwrap(), ",");
        let ranges = parse_ranges(line); 
        let id_ranges = ranges
            .iter()
            .flat_map(|range| map_id_ranges(range, vec![2]))
            .collect::<Vec<IdRange>>();

        let mut sum: u64 = 0;

        for range in id_ranges {
            sum += sum_invalid_ids(range);
        }

        sum.to_string()
    }

    fn solve_part_2(&self, lines: &Vec<String>) -> String {
        let line = format!("{}{}", lines.get(0).unwrap(), ",");
        let ranges = parse_ranges(line); 
        let id_ranges = ranges
            .iter()
            .flat_map(|range| map_id_ranges(range, vec![2, 3, 5, 7, 11]))
            .collect::<Vec<IdRange>>();

        let mut sum: u64 = 0;

        for range in id_ranges {
            sum += sum_invalid_ids(range);
        }

        sum.to_string()
    }

}

fn sum_invalid_ids(mut id_range: IdRange) -> u64 {
    let mut sum: u64 = 0;

    while id_range.value <= id_range.max {
        if id_range.value >= id_range.min && !id_range.exclude.contains(&id_range.value) {
            sum += id_range.value;
        }

        id_range.value += id_range.add_factor;
    }

    sum
}

fn parse_ranges(input: String) -> Vec<Range> {
    let mut all_ranges: Vec<Range> = Vec::new();
    let mut last_i = 0;
    let mut min = 0;

    for (i, c) in input.chars().enumerate() {
        match c {
            '-' => {
                min = input[last_i..i].parse::<u64>().unwrap();
                last_i = i + 1; 
            }
            ',' => {
                let max = input[last_i..i].parse::<u64>().unwrap();
                all_ranges.push(Range {min: min, max: max});
                last_i = i + 1;
            }
            _ => {}
        }
    }

    all_ranges
}

fn map_id_ranges(range: &Range, segment_counts: Vec<u32>) -> Vec<IdRange> {
    let mut id_ranges: Vec<IdRange> = Vec::new(); 
    
    for segment_count in segment_counts {
        let mut segment_size: u32 = 1;

        while segment_count * segment_size < u64::ilog10(range.min) + 1 {
            segment_size += 1;
        }

        while u64::pow(10, (segment_count * segment_size) - 1) < range.max {
            if let Some(id_range) = map_to_id_range(&range, segment_count, segment_size) {
                id_ranges.push(id_range);
            }
            
            segment_size += 1;
        }
    }

    id_ranges
}

fn map_to_id_range(range: &Range, segment_count: u32, segment_size: u32) -> Option<IdRange> {
    let segment_value = u64::pow(10, segment_size - 1);
    let segment_shift = segment_value * 10;

    let mut value: u64 = segment_value;
    let mut add_factor: u64 = 1;

    for _ in 0..segment_count-1 {
        value = value * segment_shift + segment_value;
        add_factor = add_factor * segment_shift + 1;
    }

    while value < range.min {
        value += add_factor;
    }

    if value <= range.max {
        Some(IdRange {
            min: range.min,
            max: u64::min(range.max, u64::pow(10, segment_count * segment_size)),
            value: value, add_factor: add_factor,
            exclude: if segment_size > 1 && segment_count > segment_size { calc_exclude(segment_count, segment_size) } else { Vec::new() }
        })
    } else {
        None
    }
}

fn calc_exclude(segment_count: u32, segment_size: u32) -> Vec<u64> {
    let mut exclude_values: Vec<u64> = Vec::new();
    let mut exclude_factor = 1;

    for _ in 0..(segment_count * segment_size) - 1 {
        exclude_factor = exclude_factor * 10 + 1;
    }

    let mut exclude = exclude_factor;
        
    for _ in 0..9 {
        exclude_values.push(exclude);
        exclude += exclude_factor;
    }

    exclude_values
}
