const numberOfWorkers = 4;
const requestURL = `http://localhost:8080/runStatistics?numberOfWorkers=${numberOfWorkers}`;
const mockFile = "mock.json";

// Uncomment once our backend allow CORS
// d3.json(requestURL, (response, error) => {
//   if (error) throw error;

//   const workersScoreData = data.map((element, index) => ({
//     workers: index + 1,
//     dynamic: element.dynamicScore,
//     genetic: element.geneticScore
//   }));

//   const workersDurationData = data.map((element, index) => ({
//     workers: index + 1,
//     dynamic: element.dynamicDuration,
//     genetic: element.geneticDuration
//   }));

//   drawWorkersScoreGraph(workersScoreData);
//   drawWorkersDurationGraph(workersDurationData);
// });

d3.json(mockFile, (error, data) => {
  if (error) throw error;

  const workersScoreData = data.map((element, index) => ({
    workers: index + 1,
    dynamic: element.dynamicScore,
    genetic: element.geneticScore
  }));

  const workersDurationData = data.map((element, index) => ({
    workers: index + 1,
    dynamic: element.dynamicDuration,
    genetic: element.geneticDuration
  }));

  drawGraph(workersScoreData, "#workers-score");
  drawGraph(workersDurationData, "#workers-duration");
});

function drawGraph(data, selector) {
  // format the data
  data.forEach(d => {
    d.workers = +d.workers;
    d.dynamic = +d.dynamic;
    d.genetic = +d.genetic;
  });

  // Set diemensions
  const margin = { top: 20, right: 20, bottom: 30, left: 50 };
  const width = 960 - margin.left - margin.right;
  const height = 500 - margin.top - margin.bottom;

  // Append graph element into DOM
  const svg = d3
    .select(selector)
    .append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  // set the ranges
  const x = d3.scaleLinear().range([0, width]);
  const y = d3.scaleLinear().range([height, 0]);

  // define the lines
  var genetic = d3
    .line()
    .x(function(d) {
      return x(d.workers);
    })
    .y(function(d) {
      return y(d.genetic);
    });

  var dynamic = d3
    .line()
    .x(function(d) {
      return x(d.workers);
    })
    .y(function(d) {
      return y(d.dynamic);
    });

  // Scale the range of the data
  x.domain(
    d3.extent(data, function(d) {
      return d.workers;
    })
  );
  y.domain([
    0,
    d3.max(data, function(d) {
      return Math.max(d.dynamic, d.genetic);
    })
  ]);

  // Add paths
  svg
    .append("path")
    .data([data])
    .attr("class", "dynamic-line")
    .attr("d", dynamic);

  svg
    .append("path")
    .data([data])
    .attr("class", "genetic-line")
    .attr("d", genetic);

  // Add the X Axis
  svg
    .append("g")
    .attr("transform", "translate(0," + height + ")")
    .call(d3.axisBottom(x).tickFormat(d3.format("d")));

  // Add the Y Axis
  svg.append("g").call(d3.axisLeft(y));
}
