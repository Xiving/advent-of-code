mod day01;

use day01::Day01;
use crate::solutions::*;

pub struct Year2025 {
    days: Vec<Box<dyn Day>>
}

impl Year2025 {

    pub fn new_boxed() -> Box<Self> {
        Box::new(
            Year2025 {
                days: vec![
                    Day01::new_boxed()
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
