mod day01;
mod day02;
mod day03;
mod day04;

use day01::Day01;
use day02::Day02;
use day03::Day03;
use day04::Day04;
use crate::solutions::*;

pub struct Year2025 {
    days: Vec<Box<dyn Day>>
}

impl Year2025 {

    pub fn new_boxed() -> Box<Self> {
        Box::new(
            Year2025 {
                days: vec![
                    Day01::new_boxed(),
                    Day02::new_boxed(),
                    Day03::new_boxed(),
                    Day04::new_boxed()
                ]
            }
        )
    }

}


impl Year for Year2025 {

    fn year_number(&self) -> u32 {
      return 2025;
    }

    fn get_days(&self) -> &Vec<Box<dyn Day>>{
        return &self.days
    }

}
