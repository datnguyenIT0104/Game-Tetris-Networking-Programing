package model;

import java.io.Serializable;

/**
 *
 * @author DatIT
 */
public class IPAddress implements Serializable{
    private static final long serialVersionUID = 20210811014L;
    private String host;
    private int port;

    public IPAddress() {
    }

    public IPAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
}
