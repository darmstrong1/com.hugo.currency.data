# com.hugo.currency.data
An application that returns the current value of crypto currencies.

# Steps to build
1. On command prompt, go to root directory of project.
2. Run 'mvn clean package'
3. Copy com.currency.hugo.data-0.0.1-SHAPSHOT.jar from target to docker.
4. Go to docker directory.
5. Run the following to create a docker image: 'docker build -t hugocurrency .'
6. Run the following to run the application to see the latest value of bitcoin: 'docker run hugocurrency bitcoin'

