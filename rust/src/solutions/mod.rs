mod year2025;

use year2025::{Year2025};
use std::fs::{File};
use std::io::{Lines, BufReader};

pub trait Day {

    fn day_number(&self) -> u32 {
        return 0;
    }

    fn solve_part_1(&self, _: &mut Lines<BufReader<File>>) -> String {
        return String::from("Not implemented!");
    }

    fn solve_part_2(&self, _:  &mut Lines<BufReader<File>>) -> String {
        return String::from("Not implemented!");
    }

}

pub trait Year {

    fn year_number(&self) -> u32;

    fn get_days(&self) -> &Vec<Box<dyn Day>>;

}

pub fn get_years() -> Vec<Box<dyn Year>> {
    vec![
       Year2025::new_boxed() 
    ]
}


