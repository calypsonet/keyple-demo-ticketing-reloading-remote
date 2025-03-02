// Copyright (c) 2023 Calypso Networks Association https://calypsonet.org/
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// SPDX-License-Identifier: MIT

using System.Configuration;
using App.application;
using App.domain.api;
using App.domain.spi;
using App.infrastructure.pcscreader;
using App.infrastructure.server;
using Microsoft.Extensions.Configuration;
using Serilog;


// Create and configure the logger
Log.Logger = new LoggerConfiguration()
    .MinimumLevel.Debug()
    .Enrich.FromLogContext()
    .WriteTo.File("logs/demo-keyple-less-.log", rollingInterval: RollingInterval.Day, retainedFileCountLimit: 7)
    .CreateLogger();

Log.Information("Starting the application");
try
{
    Log.Information("Loading paramters...");

    IConfigurationBuilder builder = new ConfigurationBuilder()
        .AddJsonFile(Path.Combine(Directory.GetCurrentDirectory(), "appsettings.json"), optional: false, reloadOnChange: false);

    // Load configuration data
    IConfigurationRoot configuration = builder.Build();

    string? serverHost = configuration.GetSection("server")?["host"];
    int serverPort = int.TryParse(configuration.GetSection("server")?["port"], out int parsedPort) ? parsedPort : -1;
    string? serverEndpoint = configuration.GetSection("server")?["endpoint"];
    string? readerName = configuration.GetSection("reader")?["name"];
    string? logLevel = configuration.GetSection("logging")?["loglevel"];

    // Handle null or missing values as needed
    if (serverHost == null)
    {
        throw new ConfigurationErrorsException("Server host is missing or null.");
    }

    if (serverPort == -1)
    {
        throw new ConfigurationErrorsException("Server port is missing or not a valid integer.");
    }

    if (serverEndpoint == null)
    {
        throw new ConfigurationErrorsException("Server endpoint is missing or null.");
    }

    if (readerName == null)
    {
        throw new ConfigurationErrorsException("Reader name is missing or null.");
    }


    Log.Information("Retrieve reader and server connectors...");

    // Get an instance of the ReaderSpi implementation
    ReaderSpi cardService = PcscReaderSpiProvider.getInstance();

    // Get an instance of the ServerSpi implementation
    ServerSpi server = ServerSpiProvider.getInstance(serverHost, serverPort, serverEndpoint);

    // Create an instance of the MainServiceApi using the reader service and server
    MainServiceApi mainService = MainServiceApiProvider.getService(cardService, readerName, server);

    Log.Information("Create and start application...");

    // Create an instance of the application and start it
    Application app = new Application(mainService);
    app.Start();
}
catch (Exception ex)
{
    Console.Error.WriteLine("Error while running the application (see log file for details).");
    Console.Error.WriteLine(ex.Message);
    Log.Fatal(ex, "The application failed.");
}
finally
{
    Console.WriteLine("Press any key to exit.");
    Console.ReadKey();
    Log.Information("Closing the application");
    Log.CloseAndFlush();
}
