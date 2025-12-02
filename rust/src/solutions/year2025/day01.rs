use crate::solutions::{*};

pub struct Day01 {
}

impl Day01 {

    pub fn new_boxed() -> Box<Self> {
        Box::new(Day01 {})
    }

}

impl Day for Day01 {

    fn day_number(&self) -> u32 {
        return 1;
    }

    fn solve_part_1(&self, lines: &Vec<String>) -> String {
        let mut rotation: i64 = 50;
        let mut zero_count: u64 = 0;

        for line in lines {
            if let Some(next_rotation) = input_to_rotation(line) {
                rotation = (rotation + next_rotation).rem_euclid(100);

                if rotation == 0 {
                    zero_count += 1;
                }
            } else {
                return String::from("Something went wrong!")
            }
        }

        return zero_count.to_string();
    }

    fn solve_part_2(&self, lines: &Vec<String>) -> String {
        let mut rotation: i64 = 50;
        let mut zero_count: u64 = 0;

        for line in lines {
            if let Some(next_rotation) = input_to_rotation(line) {
                rotation += next_rotation;

                if rotation < 0 && rotation == next_rotation {
                    zero_count += ((rotation + 1) / -100) as u64;
                } else if rotation < 0 { 
                    zero_count += ((rotation - 99) / -100) as u64;
                } else if rotation > 100 {
                    zero_count += ((rotation - 1) / 100) as u64;
                }

                rotation = rotation.rem_euclid(100);

                if rotation == 0 {
                    zero_count += 1;
                }
            } else {
                return String::from("Something went wrong!")
            }
        }

        return zero_count.to_string();
    }

}

fn input_to_rotation(line: &String) -> Option<i64> {
    return match line.chars().nth(0) {
        Some('L') => { 
            let amount = line[1..].parse::<i64>().ok()?;
            Some(amount * -1)
        }
        Some('R') => { 
            line[1..].parse().ok() 
        }
        _ => { 
            None 
        }
    }
}
