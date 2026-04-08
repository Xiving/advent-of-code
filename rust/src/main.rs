mod solutions;

use std::time::{SystemTime};
use std::io::{Result, BufRead, BufReader};
use std::fs::{File};
use solutions::{*};


fn main() {

    for year in get_years() {
        println!("┏━━━━━━┓");
        println!("┃ {:04} ┃", year.year_number());
        println!("┠──────╄━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┯━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        println!("┃ DAY  │                 SOLUTION PART 1 │                 SOLUTION PART 2 ┃");
        println!("┠──────┼─────────────────────────────────┼─────────────────────────────────┨");

        for day in year.get_days() {            
            if let Ok(lines) = read_lines_from_input_file(year.year_number(), day.day_number()) {
                let start = SystemTime::now();
                let solution_part_1 = day.solve_part_1(&lines); 
                let middle = SystemTime::now();
                let solution_part_2 = day.solve_part_2(&lines);
                let end = SystemTime::now();

                let dur_part_1 = middle.duration_since(start).unwrap().as_millis();
                let dur_part_2 = end.duration_since(middle).unwrap().as_millis();

                println!("┃ {:02}   │ {:>25}{:>4}ms │ {:>25}{:>4}ms ┃", day.day_number(), solution_part_1, dur_part_1, solution_part_2, dur_part_2);
            }
        }

        println!("┗━━━━━━┷━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┷━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");
    }

}

fn read_lines_from_input_file(year: u32, day: u32) -> Result<Vec<String>> {
    let f = File::open(format!("./input/year{:4}/day{:02}.txt", year, day))?;
    let mut f = BufReader::new(f).lines();
    
    let mut lines = Vec::new();

    while let Some(Ok(line)) = f.next() && !line.is_empty() {
       lines.push(line); 
    }

    Ok(lines)
}
