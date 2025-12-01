mod solutions;

use solutions::{*};

fn main() {

    for year in get_years() {
        for day in year.get_days() {
            println!("Year: {} day: {}", year.year_number(), day.day_number())
        }
    }

}
