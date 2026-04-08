use crate::solutions::{*};

pub struct Day03 {

}

impl Day03 {

    pub fn new_boxed() -> Box<Self> {
        Box::new(Day03 {})
    }

}

impl Day for Day03 {

    fn day_number(&self) -> u32 {
        return 3;
    }

    fn solve_part_1(&self, lines: &Vec<String>) -> String {
        let mut max_joltage_sum = 0;

        for line in lines {
            max_joltage_sum += get_max_joltage(line, 2);
        }

        return max_joltage_sum.to_string();
    }


    fn solve_part_2(&self, lines: &Vec<String>) -> String {
        let mut max_joltage_sum = 0;

        for line in lines {
            max_joltage_sum += get_max_joltage(line, 12);
        }

        return max_joltage_sum.to_string();
    }

}

fn get_max_joltage(line: &String, bank_size: usize) -> u64 {
    let mut bank: Vec<u32> = vec![0; bank_size + 1];

    let mut it = line[0..bank_size].chars().enumerate();
    while let Some((i, digit)) = it.next().and_then(|(i, c)|  c.to_digit(10).and_then(|d| Some((i, d)))) {
        bank[i] = digit;
    }

    let mut it = line[bank_size..line.len()].chars();
    while let Some(digit) = it.next().and_then(|c|  c.to_digit(10)) {
        bank = collapse(bank);
        
        if bank[bank_size - 1] < digit {
            bank[bank_size - 1 as usize] = digit;
        }
    }

    let mut joltage: u64 = 0;

    for slot in 0..bank.len()-1 {
        joltage = joltage * 10 + (bank[slot] as u64);
    }

    return joltage;
}

fn collapse(mut bank: Vec<u32>) -> Vec<u32> {
    for i in find_collapse_point(&bank)..bank.len() {
        bank[i - 1] = bank[i]; 
    }

    return bank;
}

fn find_collapse_point(bank: &Vec<u32>) -> usize {
    for i in 1..bank.len()-1 {
       if bank[i - 1] < bank[i] {
           return i;
       }
    }

    return bank.len();
}
