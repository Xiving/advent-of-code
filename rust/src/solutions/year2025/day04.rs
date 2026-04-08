use crate::solutions::{*};
use std::collections::{VecDeque};

pub struct Day04 {
    
}

impl Day04 {

    pub fn new_boxed() -> Box<Self> {
        Box::new(Day04 {})
    }

}

impl Day for Day04 {

    fn day_number(&self) -> u32 {
        return 4;
    }

    fn solve_part_1(&self, lines: &Vec<String>) -> String {
        let level = parse_level(lines); 
        let neighbour_count = count_neighbours(level);
        let mut accessible: u64 = 0;

        for y in 0..neighbour_count.len() {
            for x in 0..neighbour_count[y].len() {
                if neighbour_count[y][x] != 0 && neighbour_count[y][x] < 5 {
                    accessible += 1;
                }
            }
        }

        return accessible.to_string();
    }


    fn solve_part_2(&self, lines: &Vec<String>) -> String {
        let level = parse_level(lines); 
        let mut neighbour_count = count_neighbours(level);
        let mut queue = VecDeque::from(find_accessible(&neighbour_count));
        let mut accessible: u64 = 0;

        while let Some((y, x)) = queue.pop_front() {
            if neighbour_count[y][x] == 0 {
                continue;
            };

            accessible += 1;
            neighbour_count[y][x] = 0;

            for y_neighbour in y.checked_sub(1).unwrap_or(0)..usize::min(neighbour_count.len(), y + 2) {
                for x_neighbour in x.checked_sub(1).unwrap_or(0)..usize::min(neighbour_count.len(), x + 2) {
                    let count = neighbour_count[y_neighbour][x_neighbour];

                    if count == 0 {
                        continue;
                    }

                    if count < 6 {
                        queue.push_back((y_neighbour, x_neighbour));
                    }

                    neighbour_count[y_neighbour][x_neighbour] -= 1;
                }   
            }
        }

        return accessible.to_string();
    }

}

fn find_accessible(level: &Vec<Vec<u8>>) -> Vec<(usize, usize)> {
    let mut accessible: Vec<(usize, usize)> = Vec::new(); 

    for y in 0..level.len() {
        for x in 0..level[y].len() {
            if level[y][x] != 0 && level[y][x] < 5 {
                accessible.push((y, x));
            }
        }
    }

    accessible
}

fn count_neighbours(level: Vec<Vec<bool>>) -> Vec<Vec<u8>> {
    let mut neighbour_count: Vec<Vec<u8>> = Vec::new();

    for y in 0..level.len() {
        let mut row: Vec<u8> = Vec::new();

        for x in 0..level[y].len() {
            if !level[y][x] {
                row.push(0);
                continue;
            }

            let mut count: u8 = 0;

            for y_frame in y.checked_sub(1).unwrap_or(0)..usize::min(level.len(), y + 2) {
                for x_frame in x.checked_sub(1).unwrap_or(0)..usize::min(level.len(), x + 2) {
                    if level[y_frame][x_frame] {
                        count += 1;
                    }
                }   
            }

            row.push(count); 
        }

        neighbour_count.push(row);
    }
    
    neighbour_count
}

fn parse_level(lines: &Vec<String>) -> Vec<Vec<bool>> {
    lines.iter()
        .map(|line| line.chars().map(|c| c == '@').collect())
        .collect() 
}

