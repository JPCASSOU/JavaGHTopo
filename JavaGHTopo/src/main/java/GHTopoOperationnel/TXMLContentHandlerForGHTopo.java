
package GHTopoOperationnel;
import static GHTopoOperationnel.TXMLKeysConstants.*;
import GHTopoOperationnel.Types.TEntrance;
import GHTopoOperationnel.Types.TReseau;
import GHTopoOperationnel.Types.TSecteur;
import GHTopoOperationnel.Types.TCode;
import GHTopoOperationnel.Types.TExpe;
import GHTopoOperationnel.Types.TStation;
import GHTopoOperationnel.Types.TViseeEnAntenne;
import static GHTopoOperationnel.GeneralFunctions.*;
import org.xml.sax.*;
import org.xml.sax.helpers.LocatorImpl;

public class TXMLContentHandlerForGHTopo implements ContentHandler {
        private TToporobotStructure myDocTopo;
        private int levelElement = 0;
        private TSerie TempSerie;
        // numéro de section 0 = rien; 1 = Général; 8 = Antennes
        int currentNoReadingSection = 0;
        
        public TXMLContentHandlerForGHTopo(TToporobotStructure t) 
        {
            super();
            // On definit le locator par defaut.
            locator = new LocatorImpl();
            this.myDocTopo = t;
        }

        /**
         * Definition du locator qui permet a tout moment pendant l'analyse, de localiser
         * le traitement dans le flux. Le locator par defaut indique, par exemple, le numero
         * de ligne et le numero de caractere sur la ligne.
         * @author smeric
         * @param value le locator a utiliser.
         * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
         */
        public void setDocumentLocator(Locator value) {
                locator =  value;
        }

        /**
         * Evenement envoye au demarrage du parse du flux xml.
         * @throws SAXException en cas de probleme quelconque ne permettant pas de
         * se lancer dans l'analyse du document.
         * @see org.xml.sax.ContentHandler#startDocument()
         */
        public void startDocument() throws SAXException 
        {
                //System.out.println("Debut de l'analyse du document");
        }

        /**
         * Evenement envoye a la fin de l'analyse du flux XML.
         * @throws SAXException en cas de probleme quelconque ne permettant pas de
         * considerer l'analyse du document comme etant complete.
         * @see org.xml.sax.ContentHandler#endDocument()
         */
        public void endDocument() throws SAXException 
        {
            //    System.out.println("Fin de l'analyse du document" );
        }

        /**
         * Debut de traitement dans un espace de nommage.
         * @param prefixe utilise pour cet espace de nommage dans cette partie de l'arborescence.
         * @param URI de l'espace de nommage.
         * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
         */
        public void startPrefixMapping(String prefix, String URI) throws SAXException 
        {
            //    System.out.println("Traitement de l'espace de nommage : " + URI + ", prefixe choisi : " + prefix);
        }

        /**
         * Fin de traitement de l'espace de nommage.
         * @param prefixe le prefixe choisi a l'ouverture du traitement de l'espace nommage.
         * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
         */
        public void endPrefixMapping(String prefix) throws SAXException 
        {
            //    System.out.println("Fin de traitement de l'espace de nommage : " + prefix);
        }

        /**
         * Evenement recu a chaque fois que l'analyseur rencontre une balise XML ouvrante.
         * @param nameSpaceURI l'URL de l'espace de nommage.
         * @param localName le nom local de la balise.
         * @param rawName nom de la balise en version 1.0 <code>nameSpaceURI + ":" + localName</code>
         * @throws SAXException si la balise ne correspond pas a ce qui est attendu,
         * comme non-respect d'une DTD.
         * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        private TEntrance makeTEntrance(Attributes a)
        {
            TEntrance result = new TEntrance();
            //afficherMessage(" -- makeEntrance");
            //for (int i = 0; i < a.getLength(); i++) afficherMessage(sprintf("     - %s = %s", a.getLocalName(i), a.getValue(i)));
            result.eNumEntree   = strToIntDef(a.getValue(GTX_ATTR_ENTRANCE_IDX).trim(), 0);
            result.eNomEntree   = a.getValue(GTX_ATTR_ENTRANCE_NAME).trim();
            result.eObserv      = a.getValue(GTX_ATTR_ENTRANCE_OBS).trim();
            result.eRefSer      = strToIntDef(a.getValue(GTX_ATTR_ENTRANCE_REFSERIE).trim(), 0);
            result.eRefSt       = strToIntDef(a.getValue(GTX_ATTR_ENTRANCE_REFPT).trim(), 0);
            result.eXEntree     = ensureConvertStrToNumber(a.getValue(GTX_ATTR_ENTRANCE_X), 0.00);
            result.eYEntree     = ensureConvertStrToNumber(a.getValue(GTX_ATTR_ENTRANCE_Y), 0.00);
            result.eZEntree     = ensureConvertStrToNumber(a.getValue(GTX_ATTR_ENTRANCE_Z), 0.00);
            return result;
        }    
        private TReseau makeTReseau(Attributes a)
        {
            TReseau result = new TReseau();
            result.IdxReseau     = strToIntDef(a.getValue(GTX_ATTR_RESEAU_IDX).trim(), 0);
            result.NomReseau     = a.getValue(GTX_ATTR_RESEAU_NAME).trim();
            result.ObsReseau     = a.getValue(GTX_ATTR_RESEAU_OBS).trim();
            result.CouleurReseauR = strToIntDef(a.getValue(GTX_ATTR_RESEAU_COLOR_R).trim(), 0);
            result.CouleurReseauG = strToIntDef(a.getValue(GTX_ATTR_RESEAU_COLOR_G).trim(), 0);
            result.CouleurReseauB = strToIntDef(a.getValue(GTX_ATTR_RESEAU_COLOR_B).trim(), 0);
            result.TypeReseau    = strToIntDef(a.getValue(GTX_ATTR_RESEAU_TYPE).trim(), 0);
            return result;
        }        
        private TSecteur makeTSecteur(Attributes a)
        {
            TSecteur result = new TSecteur();
            result.IdxSecteur     = strToIntDef(a.getValue(GTX_ATTR_SECTEUR_IDX).trim(), 0);
            result.NomSecteur     = a.getValue(GTX_ATTR_SECTEUR_NAME).trim();
            result.CouleurSecteurR = strToIntDef(a.getValue(GTX_ATTR_SECTEUR_COLOR_R).trim(), 0);
            result.CouleurSecteurG = strToIntDef(a.getValue(GTX_ATTR_SECTEUR_COLOR_G).trim(), 0);
            result.CouleurSecteurB = strToIntDef(a.getValue(GTX_ATTR_SECTEUR_COLOR_B).trim(), 0);
            return result;
        }
        private TCode makeTCode(Attributes a)
        {
            TCode result = new TCode();
            result.IdxCode          = strToIntDef(a.getValue(GTX_ATTR_CODE_NUMERO).trim(), 0);
            result.UniteBoussole    = ensureConvertStrToNumber(a.getValue(GTX_ATTR_CODE_UCOMPASS), 360.00);
            result.UniteClino       = ensureConvertStrToNumber(a.getValue(GTX_ATTR_CODE_UCLINO), 360.00);
            result.FactLong         = ensureConvertStrToNumber(a.getValue(GTX_ATTR_CODE_FACT_LONG), 1.00);
            result.AngLimite        = ensureConvertStrToNumber(a.getValue(GTX_ATTR_CODE_ANGLIMITE), 360.00);
            result.PsiL             = ensureConvertStrToNumber(a.getValue(GTX_ATTR_CODE_PSI_L), 0.01);
            result.PsiAz            = ensureConvertStrToNumber(a.getValue(GTX_ATTR_CODE_PSI_A), 0.1);
            result.PsiP             = ensureConvertStrToNumber(a.getValue(GTX_ATTR_CODE_PSI_P), 0.1);
            result.Commentaire      = a.getValue(GTX_ATTR_CODE_OBS).trim();
            return result;
        }           
        private TExpe makeTExpe(Attributes a)
        {
            TExpe result = new TExpe();
            result.IdxExpe         = strToIntDef(a.getValue(GTX_ATTR_EXPE_NUMERO).trim(), 0);
            result.IdxColor        = strToIntDef(a.getValue(GTX_ATTR_EXPE_IDXCOLOR).trim(), 0);
            result.ModeDecl        = strToIntDef(a.getValue(GTX_ATTR_EXPE_MODEDECL).trim(), 0);
            result.Commentaire     = a.getValue(GTX_ATTR_EXPE_OBS).trim(); 
            result.Speleometre     = a.getValue(GTX_ATTR_EXPE_SURVEY1).trim(); 
            result.Speleographe    = a.getValue(GTX_ATTR_EXPE_SURVEY2).trim(); 
            result.DateLeve        = 0; //Date="0000-01-01" GTX_ATTR_EXPE_DATE
            result.Declinaison     = ensureConvertStrToNumber(a.getValue(GTX_ATTR_EXPE_DECLINAT), 0.00);
            result.Inclinaison     = ensureConvertStrToNumber(a.getValue(GTX_ATTR_EXPE_INCLINAT), 0.00);
            return result;
        }      
        
        private TViseeEnAntenne makeTViseeEnAntenne(Attributes a)
        {
            TViseeEnAntenne result = new TViseeEnAntenne();
            result.IDViseeAntenne   = strToIntDef(a.getValue(GTX_KEY_ANTENNA_NUMERO).trim(), 0);
            result.IDTerrainStation = a.getValue(GTX_KEY_ANTENNA_LABEL).trim();
            result.Commentaires     = a.getValue(GTX_KEY_ANTENNA_OBS).trim();
            result.Reseau           = strToIntDef(a.getValue(GTX_KEY_ANTENNA_NETWORK).trim(), 0);
            result.Secteur          = 0; // strToIntDef(a.getValue(GTX_KEY_ANTENNA_SECTEUR).trim(), 0);
            result.SerieDepart      = strToIntDef(a.getValue(GTX_KEY_ANTENNA_SERIE).trim(), 0);
            result.PtDepart         = strToIntDef(a.getValue(GTX_KEY_ANTENNA_POINT).trim(), 0);
            result.Code             = strToIntDef(a.getValue(GTX_KEY_ANTENNA_CODE).trim(), 0);
            result.Expe             = strToIntDef(a.getValue(GTX_KEY_ANTENNA_TRIP).trim(), 0);
            result.Longueur         = ensureConvertStrToNumber(a.getValue(GTX_KEY_ANTENNA_LONG), 0.00);
            result.Azimut           = ensureConvertStrToNumber(a.getValue(GTX_KEY_ANTENNA_AZIMUT), 0.00);
            result.Pente            = ensureConvertStrToNumber(a.getValue(GTX_KEY_ANTENNA_PENTE), 0.00);
            return result;
        }            
        private TStation makeTStation(Attributes a)
        {
            TStation result = new TStation();
            result.IDStation     = 0; //strToIntDef(a.getValue("").trim(), 0);
            result.IDTerrain     = a.getValue(GTX_ATTR_VISEE_LBL).trim();
            result.TypeVisee     = strToIntDef(a.getValue(GTX_ATTR_VISEE_TYPE).trim(), 0);
            result.Code          = strToIntDef(a.getValue(GTX_ATTR_VISEE_CODE).trim(), 0);
            result.Expe          = strToIntDef(a.getValue(GTX_ATTR_VISEE_EXPE).trim(), 0);
            result.Secteur       = strToIntDef(a.getValue(GTX_ATTR_VISEE_SECTEUR).trim(), 0);
            
            
            result.Longueur      = ensureConvertStrToNumber(a.getValue(GTX_ATTR_VISEE_LONG), 0.00);
            result.Azimut        = ensureConvertStrToNumber(a.getValue(GTX_ATTR_VISEE_AZ), 0.00);
            result.Pente         = ensureConvertStrToNumber(a.getValue(GTX_ATTR_VISEE_P), 0.00);
            result.LG            = ensureConvertStrToNumber(a.getValue(GTX_ATTR_VISEE_LG), 0.00);
            result.LD            = ensureConvertStrToNumber(a.getValue(GTX_ATTR_VISEE_LD), 0.00);
            result.HZ            = ensureConvertStrToNumber(a.getValue(GTX_ATTR_VISEE_HZ), 0.00);
            result.HN            = ensureConvertStrToNumber(a.getValue(GTX_ATTR_VISEE_HN), 0.00);
            result.Commentaires  = a.getValue(GTX_ATTR_VISEE_OBS).trim();
            //<Shot ="135.00" ="302.1" ="1.00" ="301" ="1.00" ="100.00" ="1.00" ="301" ="" ="1.00" ="14.140" ="2" ="" ="3"/>
            return result;           
        }        
        public void startElement(String nameSpaceURI, String localName, String rawName, Attributes attributs) throws SAXException 
        {
            levelElement++;
            //afficherMessage(sprintf("Monte au niveau %d: Ouverture de la balise : %s", levelElement, localName));
            // les différentes sections
            if (levelElement == 2)
            {
                afficherMessage(" -- Lecture de la section " + localName);
                if      (localName.equals(GTX_KEY_SECTION_GENERAL))       currentNoReadingSection = 1;
                else if (localName.equals(GTX_KEY_SECTION_ENTRANCES))     currentNoReadingSection = 2;
                else if (localName.equals(GTX_KEY_SECTION_RESEAUX))       currentNoReadingSection = 3;
                else if (localName.equals(GTX_KEY_SECTION_SECTEURS))      currentNoReadingSection = 4;
                else if (localName.equals(GTX_KEY_SECTION_CODES))         currentNoReadingSection = 5;
                else if (localName.equals(GTX_KEY_SECTION_EXPES))         currentNoReadingSection = 6;
                else if (localName.equals(GTX_KEY_SECTION_SERIES))        currentNoReadingSection = 7;
                else if (localName.equals(GTX_KEY_SECTION_ANTENNAS))      currentNoReadingSection = 8;
            }    
            if (levelElement == 3)
            switch(currentNoReadingSection)
            {
                case 0: 
                    break;
                case 1: // general
                    double x1 = 0.00;
                    double y1 = 0.00;
                    double z1 = 0.00;
                    myDocTopo.setDefaultRefSerSt(1, 0);
                    myDocTopo.setDefaultCoords(x1, y1, z1);
                    myDocTopo.setNomEtude(attributs.getValue(GTX_ATTR_NOM_ETUDE).trim());
                    myDocTopo.setCommentairesEtude(attributs.getValue(GTX_ATTR_COMMENTAIRES_ETUDE).trim());
                    // systèmes de coordonnées
                    myDocTopo.setSystemeCoordonnees(strToIntDef(attributs.getValue(GTX_ATTR_COORDS_SYSTEM_EPSG).trim(), 0),
                                                    attributs.getValue(GTX_ATTR_COORDS_SYSTEM_GHTOPO).trim(),
                                                    attributs.getValue(GTX_ATTR_COORDS_SYSTEM_DESC).trim());
                    myDocTopo.setCommentairesEtude(attributs.getValue(GTX_ATTR_COMMENTAIRES_ETUDE).trim());
                    break;
                case 2: // entrées
                    //TEntrance myEntrance = makeEntrance(attributs);
                    myDocTopo.addEntrance(makeTEntrance(attributs));
                    break; 
                case 3: // réseaux
                    myDocTopo.addReseau(makeTReseau(attributs));
                    break;
                case 4: // secteurs
                    myDocTopo.addSecteur(makeTSecteur(attributs));
                    break;
                case 5: // codes
                    myDocTopo.addCode(makeTCode(attributs));
                    break;
                case 6: // expés
                    myDocTopo.addExpe(makeTExpe(attributs));
                    break;   
                case 7: // séries
                    afficherMessage("Une série est démarrée = création de cette série");
                    // code en ligne ici: des éléments sont ajoutés
                    TempSerie = new TSerie();
                    TempSerie.IdxSerie  = strToIntDef(attributs.getValue(GTX_ATTR_SERIE_Numero).trim(), 0);
                    TempSerie.NomSerie  = attributs.getValue(GTX_ATTR_SERIE_NAME).trim();
                    TempSerie.Reseau    = strToIntDef(attributs.getValue(GTX_ATTR_SERIE_RESEAU).trim(), 0);
                    TempSerie.SerDep    = strToIntDef(attributs.getValue(GTX_ATTR_SERIE_SERDEP).trim(), 0);
                    TempSerie.PtDep     = strToIntDef(attributs.getValue(GTX_ATTR_SERIE_PTDEP).trim(), 0);
                    TempSerie.SerArr    = strToIntDef(attributs.getValue(GTX_ATTR_SERIE_SERARR).trim(), 0);
                    TempSerie.PtArr     = strToIntDef(attributs.getValue(GTX_ATTR_SERIE_PTARR).trim(), 0);
                    TempSerie.Chance    = strToIntDef(attributs.getValue(GTX_ATTR_SERIE_CHANCE).trim(), 0);
                    TempSerie.Obstacle  = strToIntDef(attributs.getValue(GTX_ATTR_SERIE_OBSTACLE).trim(), 0);
                    TempSerie.Raideur   = ensureConvertStrToNumber(attributs.getValue(GTX_ATTR_SERIE_RAIDEUR), 0.00);
                    TempSerie.ObsSerie  = attributs.getValue(GTX_ATTR_SERIE_OBS).trim();
                    
                    break;
                case 8: // antennes
                    myDocTopo.AddViseeEnAntenne(makeTViseeEnAntenne(attributs));
                    break;       
                default:
                    break;
            }        
            if ((levelElement == 4) && (currentNoReadingSection == 7))
            {
                afficherMessage("---- Lecture des stations: " + localName);
            }    
            if ((levelElement == 5) && (currentNoReadingSection == 7))
            {
                TempSerie.addStation(makeTStation(attributs));
            }    
                        
            /*
            if ( ! "".equals(nameSpaceURI)) { // espace de nommage particulier
                    System.out.println("  appartenant a l'espace de nom : "  + nameSpaceURI);
            }

            System.out.println("  Attributs de la balise : ");

            for (int index = 0; index < attributs.getLength(); index++) { // on parcourt la liste des attributs
                    System.out.println("     - " +  attributs.getLocalName(index) + " = " + attributs.getValue(index));
            }

            //*/
        }

        /**
         * Evenement recu a chaque fermeture de balise.
         * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
         */
        public void endElement(String nameSpaceURI, String localName, String rawName) throws SAXException 
        {
            levelElement--; 
            //afficherMessage(sprintf("Retourne au niveau: %d: Fermeture de la balise : %s", levelElement, localName));
            // niveau 3 = fin de la lecture des stations d'une série => ajout de ces stations
            if ((levelElement == 3) && (currentNoReadingSection == 7))
            {
                afficherMessage("---- Les stations de la série sont prêtes --> cloture et ajout");
                myDocTopo.addSerie(TempSerie);
            }    
            // niveau 0 = le document est entièrement lu
            if (levelElement == 0) afficherMessage("Le document est entièrement lu");
            /*
            if ( ! "".equals(nameSpaceURI)) { // name space non null
                    System.out.print("appartenant a l'espace de nommage : " + localName);
            }
            System.out.println();
            //*/
        }

        /**
         * Evenement recu a chaque fois que l'analyseur rencontre des caracteres (entre
         * deux balises).
         * @param ch les caracteres proprement dits.
         * @param start le rang du premier caractere a traiter effectivement.
         * @param end le rang du dernier caractere a traiter effectivement
         * @see org.xml.sax.ContentHandler#characters(char[], int, int)
         */
        public void characters(char[] ch, int start, int end) throws SAXException {
                //System.out.println("#PCDATA : " + new String(ch, start, end));
        }

        /**
         * Recu chaque fois que des caracteres d'espacement peuvent etre ignores au sens de
         * XML. C’est-a-dire que cet evenement est envoye pour plusieurs espaces se succedant,
         * les tabulations, et les retours chariot se succedant ainsi que toute combinaison de ces
         * trois types d'occurrence.
         * @param ch les caracteres proprement dits.
         * @param start le rang du premier caractere a traiter effectivement.
         * @param end le rang du dernier caractere a traiter effectivement
         * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
         */
        public void ignorableWhitespace(char[] ch, int start, int end) throws SAXException {
                //System.out.println("espaces inutiles rencontres : ..." + new String(ch, start, end) +  "...");
        }

        /**
         * Rencontre une instruction de fonctionnement.
         * @param target la cible de l'instruction de fonctionnement.
         * @param data les valeurs associees a cette cible. En general, elle se presente sous la forme 
         * d'une serie de paires nom/valeur.
         * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
         */
        public void processingInstruction(String target, String data) throws SAXException {
                System.out.println("Instruction de fonctionnement : " + target);
                System.out.println("  dont les arguments sont : " + data);
        }

        /**
         * Recu a chaque fois qu'une balise est evitee dans le traitement a cause d'un
         * probleme non bloque par le parser. Pour ma part je ne pense pas que vous
         * en ayez besoin dans vos traitements.
         * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
         */
        public void skippedEntity(String arg0) throws SAXException {
                // Je ne fais rien, ce qui se passe n'est pas franchement normal.
                // Pour eviter cet evenement, le mieux est quand meme de specifier une DTD pour vos
                // documents XML et de les faire valider par votre parser.              
        }

        private Locator locator;

}

 