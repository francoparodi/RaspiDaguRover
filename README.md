# RaspiDaguRover

A basic JSF2 (Java Server Faces) application to control a Rover, [Raspberry Pi](https://www.raspberrypi.org/) based, over a web page.

![dagu_rover_main](docs/img/dagu_rover_main.jpg)

The user (after login) can control rover movements, see environment via camera, switch on/off a frontal lamp.

![login](docs/img/login.png)

![cockpit](docs/img/cockpit.png)

Lost connection cause the rover to stop.

List of materials used to develop this rover solution:

* **Pololu Dagu Rover (with 4 motors)**
![pololu_dagu_rover](docs/img/pololu_dagu_rover.jpg)

* **Food tapperware**
![food_tapperware](docs/img/food_tapperware.jpg)

* **Raspberry Pi 3 B Model**
![raspberry_pi_3](docs/img/raspberry_pi_3_B.jpg)

* **Raspberry Pi Camera V2.1**
![raspberry_pi_camera_v21](docs/img/raspberry_pi_camera_v21.jpeg)

* **Raspberry Pi Camera case**
![raspberry_pi_camera_case](docs/img/raspberry_pi_camera_case.jpg)

* **Raspberry supply battery (10000mAh Powerbank)**
![raspberry_pi_supply_battery](docs/img/raspberry_pi_supply_battery.jpg)

* **Raspberry switch**
![raspberry_switch](docs/img/raspberry_switch.jpg)

* **Raspberry restart button**
![raspberry_restart_button](docs/img/raspberry_restart_button.jpg)

* **Motors supply battery (5000mAh LiPo battery)**
![motors_supply_battery](docs/img/motors_supply_battery.jpg)

* **4-Channel Motors controller circuit**
![4ch_motor_controller](docs/img/4ch_motor_controller.jpg)

* **Motors power switch**
![motors_switch](docs/img/motors_switch.jpg)

* **Frontal lamp with 3-AAA supply battery**
![frontal_lamp](docs/img/frontal_lamp.jpg)

* **Relè to power on-off frontal lamp**
![raspberry_pi_relay](docs/img/raspberry_pi_relay.jpg)

**Connection schema**
![connection_schema](docs/img/connection_schema.jpg)

![lamp](docs/img/lamp.jpg)

![lamp_tapperware](docs/img/lamp_tapperware.jpg)

![top](docs/img/top.jpg)

## Running

__as web application__
Export project as a common web application (war archive es. RaspiDaguRover.war) and deploy it:

```sh
java -jar payara-micro.jar --deploy /home/user/RaspiDaguRover.war
```

## Authors 

Franco Parodi <franco.parodi@aol.com>

