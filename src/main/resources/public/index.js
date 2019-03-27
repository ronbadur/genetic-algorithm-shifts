function showSlideValue(sliderId, spanId) {
    var value = document.getElementById(sliderId).value;
    var span = document.getElementById(spanId);
    span.innerHTML = "Value: " + value / 10;
}

const geneticParameters = [];
const geneticResults = [];

document.getElementById('run-algorithm').addEventListener('click', ()=>{
	const mutationRate = document.getElementById('mutationSilder').value;
	const crossoverRate = document.getElementById('crossoverSlider').value;
	const randomizeRate = document.getElementById('randomizeSlider').value;
	const populationSize = document.getElementById('populationSize').value;
	
	fetch(`http://localhost:8080/genetic?mutationRate=${mutationRate}&crossoverRate=${crossoverRate}&randomizeRate=${randomizeRate}&populationSize=${populationSize}`)
	.then(score=>{
		geneticParameters.push({
			mutationRate,
			crossoverRate,
			randomizeRate,
			populationSize
		});
		
		geneticResults.push(score);
		
		createTableElement(geneticResults.length, geneticParameters[geneticParameters.length - 1], score);
		
		// @TODO: Build another point on the graph with the function at the bottom
	});
});

function createTableElement(index, params, score){
	var tr = document.createElement('tr');
	
	var tdIndex = document.createElement('td');
	tdIndex.appendChild(document.createTextNode(index));	
	
	var tdParams = document.createElement('td');
	tdParams.appendChild(document.createTextNode(JSON.stringify(params)));
	
	var tdScore = document.createElement('td');
	tdScore.appendChild(document.createTextNode(score));	
	
	tr.appendChild(tdIndex);
	tr.appendChild(tdParams);
	tr.appendChild(tdScore);
	
	document.getElementById('table-body').appendChild(tr);
}



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
    .call(
      d3
        .axisBottom(x)
        .ticks(data.length)
        .tickFormat(d3.format("d"))
    );

  // Add the Y Axis
  svg.append("g").call(d3.axisLeft(y));
}
