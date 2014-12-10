package com.example.medical.pillar;

import android.content.Context;

import com.example.medical.db.Pill;
import com.example.medical.db.PillDataAccessor;
import com.example.medical.db.PillPrescriptionJoin;
import com.example.medical.db.Prescription;
import com.example.medical.db.PrescriptionDataAccessor;
import com.example.medical.db.PrescriptionPillJoinDataAccessor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by meanheffry on 11/11/14.
 */
public class DispenserCoordinator {
    private PrescriptionDataAccessor PresDA;
    private PillDataAccessor PillDA;
    private PrescriptionPillJoinDataAccessor JoinDA;

    private ArrayList<Prescription> Prescriptions;
    private ArrayList<Pill> Pills;
    private ArrayList<PillPrescriptionJoin> joins;
    private ArrayList<byte[]> rfids;




    private final byte[] rfid1 = new byte[]{0x1A, (byte)0xE2, 0x41, (byte)0xD9, 0x00, 0x00, 0x00, 0x3B};

    private final byte[] rfid2 = new byte[]{(byte)0xAA, 0x79, (byte)0x9B, 0x23, 0x00, 0x00, 0x00, 0x3B};


    public DispenserCoordinator(Context context){
        PresDA = new PrescriptionDataAccessor(context);
        PresDA.open();
        PillDA = new PillDataAccessor(context);
        PillDA.open();
        JoinDA = new PrescriptionPillJoinDataAccessor(context);
        JoinDA.open();

        Pills =(ArrayList)PillDA.getAllPills();
        Prescriptions = (ArrayList)PresDA.getAllPrescriptions();
        rfids= getRfids();
    }


    public ArrayList<byte[]> getRfids(){
        ArrayList<byte[]> ids = new ArrayList<byte[]>();
        for(int i=0;i<Prescriptions.size();i++){
            ids.add(Prescriptions.get(i).getRfid());
        }
        return ids;
    }



    public ArrayList<Integer> dispense(byte[] b){
        System.out.println("Dispense request from byte "+b);
        ArrayList<Integer> tubeNumbers = new ArrayList<Integer>();
        ArrayList<PillPrescriptionJoin> joins = new ArrayList<PillPrescriptionJoin>();
        long prescriptionId=0;
//        for (int j=0; j<rfids.size();j++) {
//            if (Arrays.equals(rfids.get(j), b)){
//                System.out.println("It recognizes that this is one we want");
                for (int i = 0; i < Prescriptions.size(); i++) {
                    System.out.println("Looking for the prescription this tag belongs to");
                    if (Arrays.equals(Prescriptions.get(i).getRfid(), b)){

                        System.out.println("We found the Prescription");
                        prescriptionId = Prescriptions.get(i).getId();
                        joins = (ArrayList) JoinDA.getJoinsByPrescriptionId((int) prescriptionId);
                    }
                }
                for (int i = 0; i < joins.size(); i++) {
                    Pill p =PillDA.getPillById(joins.get(i).getPillId());
                    PillDA.decrementPill(p.getId());
                    tubeNumbers.add(Integer.parseInt(p.getTube()));
                }
//            }
//        }
        return tubeNumbers;
    }



}
