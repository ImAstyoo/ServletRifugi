# ServletRifugi
Servlet per educazione civica con gli opendata dei rifugi di bolzano

La professoressa DEE MARTEENO ci ha chiesto di fare una servlet sugli open data per educazione civica.

La Web-API:

Metodo : GET
URL                                 Risposta
http://.../Rifugi_Alpini/lista        <rifugi>
                                        <rifugio>
                                          <comune>..</comune>
                                          <nome>...</nome>
                                          <telefono>...</telofono>
                                        </rifugio>
                                        ...
                                     </rifugi>

Metodo : GET
URL                                             Risposta
http://.../Rifugi_Alpini/rifugi?comune=...        <rifugi>
                                                    <rifugio>
                                                      <comune>...</comune>
                                                      <nome>...</nome>
                                                      <telefono>...</telefono>
                                                    </rifugio>
                                                    ...
                                                  </rifugi>

