const express = require('express')
const exec = require('child_process').exec;
const app = express();

app.post('/test', (req, res) => {
    res.json({ message: 'great success' })
        //res.send(JSON.stringify("Great Success"))
})


app.get('/action', (req, res) => {
    console.log('Launching python');
    // // const process = spawn('python', ['./Summarizer/main.py', 3, `New York City (NYC), often called simply New York, is the most populous city in the United States. With an estimated 2019 population of 8,336,817 distributed over about 302.6 square miles (784 km2), New York City is also the most densely populated major city in the United States.[11] Located at the southern tip of the State of New York, the city is the center of the New York metropolitan area, the largest metropolitan area in the world by urban landmass.[12] With almost 20 million people in its metropolitan statistical area and approximately 23 million in its combined statistical area, it is one of the worlds most populous megacities. New York City has been described as the cultural, financial, and media capital of the world, significantly influencing commerce,[13] entertainment, research, technology, education, politics, tourism, art, fashion, and sports. Home to the headquarters of the United Nations,[14] New York is an important center for international diplomacy,[15][16] and has sometimes been nicknamed the Capital of the World ".[17][18] Situated on one of the worlds largest natural harbors, New York City is composed of five boroughs, each of which is a county of the State of New York. The five boroughs—Brooklyn, Queens, Manhattan, the Bronx, and Staten Island—were created when local governments were consolidated into a single city in 1898.[19] The city and its metropolitan area constitute the premier gateway for legal immigration to the United States. As many as 800 languages are spoken in New York,[20] making it the most linguistically diverse city in the world. New York is home to more than 3.2 million residents born outside the United States,[21] the largest foreign-born population of any city in the world as of 2016.[22][23] As of 2019, the New York metropolitan area is estimated to produce a gross metropolitan product (GMP) of $2.0 trillion. If the New York metropolitan area were a sovereign state, it would have the eighth-largest economy in the world. New York is home to the highest number of billionaires of any city in the world.[24] New York City traces its origins to a trading post founded by colonists from the Dutch Republic in 1624 on Lower Manhattan; the post was named New Amsterdam (Dutch: Nieuw Amsterdam) in 1626.[25] The city and its surroundings came under English control in 1664 and were renamed the Province of New York after King Charles II of England granted the lands to his brother, the Duke of York.[25][26] The city was regained by the Dutch in July 1673 and was subsequently renamed New Orange for one year and three months; the city has been continuously named New York since November 1674.[27][28] New York City was the capital of the United States from 1785 until 1790,[29] and has been the largest U.S. city since 1790.[30] The Statue of Liberty greeted millions of immigrants as they came to the U.S. by ship in the late 19th and early 20th centuries,[31] and is a symbol of the U.S. and its ideals of liberty and peace.[32] In the 21st century, New York has emerged as a global node of creativity, entrepreneurship,[33] and environmental sustainability,[34][35] and as a symbol of freedom and cultural diversity.[36] In 2019, New York was voted the greatest city in the world per a survey of over 30,000 people from 48 cities worldwide, citing its cultural diversity.Many districts and landmarks in New York City are well known, including three of the worlds ten most visited tourist attractions in 2013.[38] A record 62.8 million tourists visited New York City in 2017. Times Square is the brightly illuminated hub of the Broadway Theater District, [39] one of the world s busiest pedestrian intersections, [40][41] and a major center of the world s entertainment industry.[42] Many of the city s landmarks, skyscrapers, [43] and parks are known around the world.Manhattans real estate market is among the most expensive in the world.[44][45] Providing continuous 24 / 7 service and contributing to the nickname The City That Never Sleeps, the New York City Subway is the largest single - operator rapid transit system worldwide, with 472 rail stations.The city has over 120 colleges and universities, including Columbia University, New York University, Rockefeller University, and the City University of New York system, which is the largest urban public university system in the United States.[46] Anchored by Wall Street in the Financial District of Lower Manhattan, New York City has been called both the worlds leading financial center and the most financially powerful city in the world, and is home to the worlds two largest stock exchanges by total market capitalization, the New York Stock Exchange and NASDAQ `]);
    // process.stdout.on('data', data => {
    //     console.log(data.toString());
    // });

    //let string = req.body.sentences;

    //Createing a req.number of sentences and text

    let string = "Brazil is a city in ireland. Brazil is my favourite place to be. Ireland is in europe. Cheese baby!!!";

    const command = `python ./Summarizer/main.py 3 "Brazil is a city in ireland. Brazil is my favourite place to be. Ireland is in europe. Cheese baby!!!"`;

    exec(command, (err, stdout, stderr) => {
        if (err) {
            console.error(`exec error: ${err}`);
            return;
        }
        console.log(`Number of files ${stdout}`);

        res.json(stdout);
    });
})


const port = 5000;
app.listen(port, () => {
    console.log(`listening to port ${port}`);
})