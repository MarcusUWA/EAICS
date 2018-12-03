#include <stdio.h>     // Standard input/output definitions
#include <string.h>    // String function definitions
#include <unistd.h>   // UNIX standard function definitions
#include <fcntl.h>      // File control definitions
#include <errno.h>     // Error number definitions
#include <termios.h>  // POSIX terminal control definitions
#include <time.h>
#include <stdbool.h> //For boolean usage
#include <stdlib.h>

int fd;

int  SERInit(char* path, int baud, int handshake);
int  SERSendChar(int interface, char ch);
char SERReceiveChar(int interface);
bool SERCheck(int interface);
int  SERFlush(int interface);
int  SERClose(int interface);

#define BUFFER_SIZE 100

int main(void)
{
	char path[] = "/dev/ttyUSB0";
	char buffer[BUFFER_SIZE];

	SERInit(path, 9600, 0);

	int startCounter = 0;
	char newC = 'a';
	char oldC = 'a';
	int first = 1;
	int commaCount = 0;
	int index;
	char outputString[200] = "";
	while(1)
	{
		newC = SERReceiveChar(0);
		
		if(startCounter < 174)
		{
			startCounter++;
		}
		else
		{
			if(newC == '\n' && oldC == '\n' && first == 0)
			{
				//Do nothing
			}
			else
			{
				outputString[index] = newC;
				index++;
				//printf("%c", newC);
				first = 0;
			}

			if(newC == ',')
			{
				commaCount++;
			}

			if(commaCount == 6)
			{
				outputString[index] = '\0';
				index = 0;
				printf("%s", outputString);
				fflush(stdout);
				commaCount = 0;
			}
		}
				
		oldC = newC;
	}

	SERFlush(0);
	SERClose(0);

	return 0;	
}


/**
 * \brief   sets the attributes for the port
 * \param   fd      the port handle
 * \param   speed   the baud rate
 * \param   parity  the parity of the port
 * \param   handshake the handshake type
 * \return  0 on success, -1 otherwise
 */
int SERSet_interface_attribs (int fd, int speed, int parity, int handshake) {
    struct termios tty;
    memset (&tty, 0, sizeof tty);
    if (tcgetattr (fd, &tty) != 0)
    {
        printf("error from tcgetattr\n");
        return -1;
    }
    
    cfsetospeed (&tty, speed);
    cfsetispeed (&tty, speed);
    
    //start control flags
    tty.c_cflag = (tty.c_cflag & ~CSIZE) | CS8;     // 8-bit chars
    tty.c_cflag |= (CLOCAL | CREAD);// ignore modem controls,
    // enable reading
    tty.c_cflag &= ~(PARENB | PARODD);      // shut off parity
    tty.c_cflag |= parity;
    tty.c_cflag &= ~CSTOPB;
    //tty.c_cflag &= ~CRTSCTS;		// added back in for 0a0a-bug TB @@@
    
    //start input mode flags
    
    // disable IGNBRK for mismatched speed tests; otherwise receive break
    // as \000 chars
     tty.c_iflag &= ~IGNBRK;
   // tty.c_iflag |= IGNBRK;         // disable break processing
   // tty.c_iflag |= IGNCR;
    
    tty.c_iflag &= ~(IXON | IXOFF | IXANY); // shut off xon/xoff ctrl
    
    //start local flags
    tty.c_lflag = 0;                // no signaling chars, no echo,

    
    //start output modes flags
    
    // no canonical processing
    tty.c_oflag = 0;                // no remapping, no delays
    
    //start special flags
    tty.c_cc[VMIN]  = 0;            // read doesn't block
    tty.c_cc[VTIME] = 5;            // 0.5 seconds read timeout

    
    if (tcsetattr (fd, TCSANOW, &tty) != 0)
    {
        printf("error from tcsetattr\n");
        return -1;
    }
    return 0;
}

/**
 * \brief   Sets the blocking flags for the serial port
 * \param   fd      the port handle
 * \param   should_block   an boolean whether to block port or not
 */
void SERSet_blocking (int fd, int should_block) {
    struct termios tty;
    memset (&tty, 0, sizeof tty);
    if (tcgetattr (fd, &tty) != 0)
    {
        printf("error from tggetattr\n");
        return;
    }
    
    tty.c_cc[VMIN]  = should_block ? 1 : 0;
    tty.c_cc[VTIME] = 5;            // 0.5 seconds read timeout
}

/**
 * \brief   Initialises the port
 * \param   interface the inteface type
 * \param   baud      the baud rate
 * \param   handshake  the handshake type
 * \return  0 on success, 1 otherwise
 */
int SERInit(char* path, int baud, int handshake){
    //opening the port
    //fd = open (path, O_RDWR | O_NOCTTY | O_NDELAY);
    fd = open (path, O_RDWR | O_NOCTTY | O_SYNC);
    
    if (fd < 0)
    {
        fprintf(stderr, "error opening port\n");
        return 1;
    }
    
    switch(baud) {
        case 0:
            SERSet_interface_attribs (fd, B0, 0, handshake);
            break;
        case 50:
            SERSet_interface_attribs (fd, B50, 0, handshake);
            break;
        case 75:
            SERSet_interface_attribs (fd, B75, 0, handshake);
            break;
        case 110:
            SERSet_interface_attribs (fd, B110, 0, handshake);
            break;
        case 134:
            SERSet_interface_attribs (fd, B134, 0, handshake);
            break;
        case 150:
            SERSet_interface_attribs (fd, B150, 0, handshake);
            break;
        case 200:
            SERSet_interface_attribs (fd, B200, 0, handshake);
            break;
        case 300:
            SERSet_interface_attribs (fd, B300, 0, handshake);
            break;
        case 600:
            SERSet_interface_attribs (fd, B600, 0, handshake);
            break;
        case 1200:
            SERSet_interface_attribs (fd, B1200, 0, handshake);
            break;
        case 1800:
            SERSet_interface_attribs (fd, B1800, 0, handshake);
            break;
        case 2400:
            SERSet_interface_attribs (fd, B2400, 0, handshake);
            break;
        case 4800:
            SERSet_interface_attribs (fd, B4800, 0, handshake);
            break;
        case 9600:
            SERSet_interface_attribs (fd, B9600, 0, handshake);
            break;
        case 19200:
            SERSet_interface_attribs (fd, B19200, 0, handshake);
            break;
        case 38400:
            SERSet_interface_attribs (fd, B38400, 0, handshake);
            break;
        case 57600:
            SERSet_interface_attribs (fd, B57600, 0, handshake);
            break;
        case 115200:
            SERSet_interface_attribs (fd, B115200, 0, handshake);
            break;
        case 230400:
            SERSet_interface_attribs (fd, B230400, 0, handshake);
            break;
        default:
            fprintf(stdout, "invalid baud rate!!!\n");
            return 1;
    }
    
    //setting blocking
    SERSet_blocking (fd, 0);                // set no blocking
    return 0;
}

/**
 * \brief   Sends a single character to the given port handle
 * \param   interface the inteface type
 * \param   ch      the character to send
 * \return  0 on success, 1 otherwise
 */
int SERSendChar(int interface, char ch) 
{
    char temp[1];
    temp[0]=ch;
    int n = write(fd,temp,1);
    if (n==1) {
        return 0;
    }
    else {
        return 1;
    }
}

/**
 * \brief   Receives a single character from the given port handle
 * \param   interface the inteface type
 * \return  the character received
 */
char SERReceiveChar(int interface) 
{
    char ch[1];
    int n = read(fd,ch,1);
    return ch[0];
}

/**
 * \brief   flushes the port
 * \param   interface the inteface type
 * \return  0 on success, 1 otherwise
 */
int SERFlush(int interface) 
{
    //1000 was good
    usleep(3000);
    
    tcflush(fd,TCIOFLUSH);
    
    //usleep(3000); //required to make flush work, for some reason
    return 0;
}

/**
 * \brief   closes the port
 * \param   interface the inteface type
 * \return  0 on success, 1 otherwise
 */
int SERClose(int interface) 
{
    close(fd);
    return 0;
}