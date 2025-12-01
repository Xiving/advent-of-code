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

    fn solve_part_1(&self, _: &mut Lines<BufReader<File>>) -> String {
        return String::from("Not implemented!");
    }

    fn solve_part_2(&self, _:  &mut Lines<BufReader<File>>) -> String {
        return String::from("Not implemented!");
    }


}
